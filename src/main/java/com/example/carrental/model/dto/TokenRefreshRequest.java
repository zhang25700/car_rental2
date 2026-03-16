package com.example.carrental.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRefreshRequest {

    @NotBlank(message = "refreshToken cannot be blank")
    private String refreshToken;
}
