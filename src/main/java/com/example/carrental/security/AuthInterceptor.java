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
public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REFRESH_TOKEN = "X-Refresh-Token";
    private static final String NEW_ACCESS_TOKEN = "X-New-Access-Token";
    private static final String NEW_REFRESH_TOKEN = "X-New-Refresh-Token";

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final com.example.carrental.config.JwtProperties jwtProperties;

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

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }

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

    public static String buildRefreshTokenKey(Long userId) {
        return "auth:refresh:" + userId;
    }
}
