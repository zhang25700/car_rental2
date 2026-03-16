package com.example.carrental.model.dto;

import lombok.Data;

@Data
public class OrderQueryRequest {

    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String orderNo;
    private Long customerId;
    private Long vehicleId;
    private String orderStatus;
    private String pickupDateStart;
    private String pickupDateEnd;
}
