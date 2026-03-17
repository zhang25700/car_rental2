package com.example.carrental.security;

import com.example.carrental.common.BusinessException;
import com.example.carrental.common.ErrorCode;
import com.example.carrental.context.UserContext;
import com.example.carrental.model.auth.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
/**
 * 统一认证拦截器。
 * 负责校验 Access Token、写入当前登录用户上下文，并在令牌即将过期时尝试无感刷新。
 */
public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REFRESH_TOKEN = "X-Refresh-Token";
    private static final String NEW_ACCESS_TOKEN = "X-New-Access-Token";
    private static final String NEW_REFRESH_TOKEN = "X-New-Refresh-Token";

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final com.example.carrental.config.JwtProperties jwtProperties;

    /**
     * 在控制器执行前完成认证。
     * 认证成功后会把当前用户放入 ThreadLocal，供后续业务代码直接获取。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader(AUTHORIZATION);
        if (!StringUtils.startsWith(header, BEARER_PREFIX)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "missing access token");
        }

        String accessToken = StringUtils.substringAfter(header, BEARER_PREFIX);
        try {
            Claims claims = jwtTokenProvider.parse(accessToken);
            if (!"access".equals(claims.get("tokenType", String.class))) {
                throw new BusinessException(ErrorCode.UNAUTHORIZED, "invalid access token");
            }
            // 从 Token 还原最小用户信息并绑定到当前线程。
            LoginUser loginUser = jwtTokenProvider.toLoginUser(claims);
            UserContext.set(loginUser);
            refreshWhenNecessary(request, response, loginUser, claims.getExpiration().toInstant());
            return true;
        } catch (ExpiredJwtException ex) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "access token expired");
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            log.warn("token validate failed", ex);
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "token validation failed");
        }
    }

    /**
     * 请求结束后清理 ThreadLocal，避免线程复用导致用户信息串用。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }

    /**
     * Access Token 快过期时，根据 Refresh Token 和 Redis 会话状态换发一组新令牌。
     * 新令牌通过响应头回传给前端，前端可直接覆盖本地缓存，实现无感刷新。
     */
    private void refreshWhenNecessary(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser, Instant accessExpireAt) {
        long remainMinutes = Duration.between(Instant.now(), accessExpireAt).toMinutes();
        if (remainMinutes > jwtProperties.getRefreshThresholdMinutes()) {
            return;
        }

        String refreshToken = request.getHeader(REFRESH_TOKEN);
        if (StringUtils.isBlank(refreshToken)) {
            return;
        }

        Claims claims = jwtTokenProvider.parse(refreshToken);
        if (!"refresh".equals(claims.get("tokenType", String.class))) {
            return;
        }
        String key = buildRefreshTokenKey(loginUser.getUserId());
        Object cachedToken = redisTemplate.opsForValue().get(key);
        if (!refreshToken.equals(cachedToken)) {
            return;
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(loginUser);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(loginUser);
        redisTemplate.opsForValue().set(key, newRefreshToken, jwtProperties.getRefreshTokenExpireDays(), TimeUnit.DAYS);
        response.setHeader(NEW_ACCESS_TOKEN, newAccessToken);
        response.setHeader(NEW_REFRESH_TOKEN, newRefreshToken);
    }

    /**
     * 统一约定 Refresh Token 在 Redis 中的 Key 格式。
     */
    public static String buildRefreshTokenKey(Long userId) {
        return "auth:refresh:" + userId;
    }
}
