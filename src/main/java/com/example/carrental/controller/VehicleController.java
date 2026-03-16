package com.example.carrental.controller;

import com.example.carrental.common.ApiResponse;
import com.example.carrental.model.dto.VehicleQueryRequest;
import com.example.carrental.model.entity.Vehicle;
import com.example.carrental.model.vo.PageResult;
import com.example.carrental.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ApiResponse<PageResult<Vehicle>> page(VehicleQueryRequest request) {
        return ApiResponse.success(vehicleService.page(request));
    }

    @GetMapping("/hot")
    public ApiResponse<List<Vehicle>> listHotVehicles() {
        return ApiResponse.success(vehicleService.listHotVehicles());
    }

    @PostMapping
    public ApiResponse<Void> save(@RequestBody Vehicle vehicle) {
        vehicleService.save(vehicle);
        return ApiResponse.success("saved", null);
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        vehicleService.updateStatus(id, status);
        return ApiResponse.success("updated", null);
    }
}
