package com.example.carrental.controller;

import com.example.carrental.common.ApiResponse;
import com.example.carrental.model.dto.LoginRequest;
import com.example.carrental.model.dto.RegisterRequest;
import com.example.carrental.model.dto.TokenRefreshRequest;
import com.example.carrental.model.vo.LoginResponse;
import com.example.carrental.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
/**
 * 认证相关接口。
 * 包含登录、注册和刷新令牌三个入口。
 */
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录接口。
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }

    /**
     * 用户注册接口。
     */
    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ApiResponse.success("注册成功", null);
    }

    /**
     * 刷新令牌接口。
     */
    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        return ApiResponse.success(authService.refresh(request));
    }
}
