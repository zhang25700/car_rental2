package com.example.carrental.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String issuer;
    private String secret;
    private long accessTokenExpireMinutes;
    private long refreshTokenExpireDays;
    private long refreshThresholdMinutes;
}
