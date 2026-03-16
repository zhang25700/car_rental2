package com.example.carrental.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Vehicle {

    private Long id;
    private String vehicleNo;
    private String brand;
    private String model;
    private String color;
    private Integer seatCount;
    private BigDecimal dailyPrice;
    private String status;
    private Integer hotFlag;
    private String coverImage;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
