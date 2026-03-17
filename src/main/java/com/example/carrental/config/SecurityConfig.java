package com.example.carrental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
/**
 * 安全相关 Bean 配置。
 * 当前主要提供 BCryptPasswordEncoder，用于密码加密和登录校验。
 */
public class SecurityConfig {

    /**
     * 注入统一密码编码器。
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
