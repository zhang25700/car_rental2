package com.example.carrental.service;

import com.example.carrental.model.dto.VehicleQueryRequest;
import com.example.carrental.model.entity.Vehicle;
import com.example.carrental.model.vo.PageResult;

import java.util.List;

public interface VehicleService {

    PageResult<Vehicle> page(VehicleQueryRequest request);

    List<Vehicle> listHotVehicles();

    void save(Vehicle vehicle);

    void updateStatus(Long id, String status);
}
