package com.example.carrental.model.dto;

import lombok.Data;

@Data
public class VehicleQueryRequest {

    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String brand;
    private String model;
    private String status;
    private Integer seatCount;
    private String keyword;
}
