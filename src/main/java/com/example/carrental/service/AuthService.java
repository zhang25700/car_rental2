package com.example.carrental.service;

import com.example.carrental.model.dto.LoginRequest;
import com.example.carrental.model.dto.TokenRefreshRequest;
import com.example.carrental.model.vo.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    LoginResponse refresh(TokenRefreshRequest request);
}
