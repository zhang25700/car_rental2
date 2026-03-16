package com.example.carrental.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private long accessTokenExpireAt;
    private long refreshTokenExpireAt;
}
