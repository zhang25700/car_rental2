package com.example.carrental.service.impl;

import com.example.carrental.common.BusinessException;
import com.example.carrental.common.ErrorCode;
import com.example.carrental.config.JwtProperties;
import com.example.carrental.mapper.CustomerMapper;
import com.example.carrental.mapper.SysUserMapper;
import com.example.carrental.model.auth.LoginUser;
import com.example.carrental.model.dto.LoginRequest;
import com.example.carrental.model.dto.RegisterRequest;
import com.example.carrental.model.dto.TokenRefreshRequest;
import com.example.carrental.model.entity.SysUser;
import com.example.carrental.model.entity.Customer;
import com.example.carrental.model.vo.LoginResponse;
import com.example.carrental.security.AuthInterceptor;
import com.example.carrental.security.JwtTokenProvider;
import com.example.carrental.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;
    private final CustomerMapper customerMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtProperties jwtProperties;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        SysUser user = sysUserMapper.selectByUsername(request.getUsername());
        if (user == null || user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "user not found or disabled");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "username or password incorrect");
        }

        LoginUser loginUser = new LoginUser(user.getId(), user.getUsername(), user.getRole());
        return createTokens(loginUser);
    }

    @Override
    public void register(RegisterRequest request) {
        if (sysUserMapper.selectByUsername(request.getUsername()) != null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setRole("USER");
        user.setStatus(1);
        sysUserMapper.insert(user);

        Customer customer = new Customer();
        customer.setCustomerName(request.getRealName());
        customer.setPhone(request.getUsername());
        customer.setGender("未设置");
        customer.setDriverLicenseNo("DL" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase());
        customer.setIdCardNo("ID" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase());
        customer.setStatus(1);
        customerMapper.insert(customer);
    }

    @Override
    public LoginResponse refresh(TokenRefreshRequest request) {
        Claims claims = jwtTokenProvider.parse(request.getRefreshToken());
        if (!"refresh".equals(claims.get("tokenType", String.class))) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "invalid refresh token");
        }
        LoginUser loginUser = jwtTokenProvider.toLoginUser(claims);
        String redisKey = AuthInterceptor.buildRefreshTokenKey(loginUser.getUserId());
        Object cachedToken = redisTemplate.opsForValue().get(redisKey);
        if (!request.getRefreshToken().equals(cachedToken)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "refresh token expired");
        }
        return createTokens(loginUser);
    }

    private LoginResponse createTokens(LoginUser loginUser) {
        String accessToken = jwtTokenProvider.generateAccessToken(loginUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(loginUser);
        redisTemplate.opsForValue().set(
                AuthInterceptor.buildRefreshTokenKey(loginUser.getUserId()),
                refreshToken,
                jwtProperties.getRefreshTokenExpireDays(),
                TimeUnit.DAYS
        );
        long accessExpireAt = Instant.now().plus(jwtProperties.getAccessTokenExpireMinutes(), ChronoUnit.MINUTES).toEpochMilli();
        long refreshExpireAt = Instant.now().plus(jwtProperties.getRefreshTokenExpireDays(), ChronoUnit.DAYS).toEpochMilli();
        return new LoginResponse(accessToken, refreshToken, loginUser.getRole(), accessExpireAt, refreshExpireAt);
    }
}
