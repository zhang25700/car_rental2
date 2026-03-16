package com.example.carrental.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RentalOrder {

    private Long id;
    private String orderNo;
    private Long customerId;
    private Long vehicleId;
    private LocalDateTime pickupTime;
    private LocalDateTime returnTime;
    private BigDecimal dailyRent;
    private BigDecimal totalAmount;
    private String orderStatus;
    private String remark;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
