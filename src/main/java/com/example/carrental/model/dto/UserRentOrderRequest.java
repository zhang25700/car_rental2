package com.example.carrental.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserRentOrderRequest {

    @NotNull(message = "车辆不能为空")
    private Long vehicleId;

    @NotBlank(message = "取车时间不能为空")
    private String pickupTime;

    @NotBlank(message = "还车时间不能为空")
    private String returnTime;

    @NotNull(message = "日租金不能为空")
    private BigDecimal dailyRent;

    @NotNull(message = "总金额不能为空")
    private BigDecimal totalAmount;

    private String remark;
}
