package com.example.carrental.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Customer {

    private Long id;
    private String customerName;
    private String phone;
    private String gender;
    private String driverLicenseNo;
    private String idCardNo;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
