package com.example.carrental.security;

import com.example.carrental.config.JwtProperties;
import com.example.carrental.model.auth.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@RequiredArgsConstructor
/**
 * JWT 工具类。
 * 负责令牌生成、签名校验、Claims 解析，以及 Token 与系统用户对象之间的转换。
 */
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    /**
     * 应用启动时根据配置中的密钥初始化签名器。
     */
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成短期有效的 Access Token，给业务接口鉴权使用。
     */
    public String generateAccessToken(LoginUser loginUser) {
        Instant now = Instant.now();
        Instant expireAt = now.plus(jwtProperties.getAccessTokenExpireMinutes(), ChronoUnit.MINUTES);
        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(String.valueOf(loginUser.getUserId()))
                .claim("username", loginUser.getUsername())
                .claim("role", loginUser.getRole())
                .claim("tokenType", "access")
                .issuedAt(Date.from(now))
                .expiration(Date.from(expireAt))
                .signWith(secretKey)
                .compact();
    }

    /**
     * 生成长期有效的 Refresh Token，给续签流程使用。
     */
    public String generateRefreshToken(LoginUser loginUser) {
        Instant now = Instant.now();
        Instant expireAt = now.plus(jwtProperties.getRefreshTokenExpireDays(), ChronoUnit.DAYS);
        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(String.valueOf(loginUser.getUserId()))
                .claim("username", loginUser.getUsername())
                .claim("role", loginUser.getRole())
                .claim("tokenType", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(expireAt))
                .signWith(secretKey)
                .compact();
    }

    /**
     * 校验并解析 JWT。
     * 非法签名、过期等异常由上层统一捕获并转换为业务错误。
     */
    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 将 Token 中的用户字段还原成系统内部的登录用户对象。
     */
    public LoginUser toLoginUser(Claims claims) {
        return new LoginUser(
                Long.valueOf(claims.getSubject()),
                claims.get("username", String.class),
                claims.get("role", String.class)
        );
    }
}
