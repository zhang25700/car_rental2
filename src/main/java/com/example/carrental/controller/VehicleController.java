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
/**
 * 车辆管理接口。
 * 同时服务后台车辆管理页和用户端热点/可租车辆展示。
 */
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * 后台车辆分页查询。
     */
    @GetMapping
    public ApiResponse<PageResult<Vehicle>> page(VehicleQueryRequest request) {
        return ApiResponse.success(vehicleService.page(request));
    }

    /**
     * 查询热点车辆列表。
     */
    @GetMapping("/hot")
    public ApiResponse<List<Vehicle>> listHotVehicles() {
        return ApiResponse.success(vehicleService.listHotVehicles());
    }

    /**
     * 新增或修改车辆。
     */
    @PostMapping
    public ApiResponse<Void> save(@RequestBody Vehicle vehicle) {
        vehicleService.save(vehicle);
        return ApiResponse.success("保存成功", null);
    }

    /**
     * 单独更新车辆状态。
     */
    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        vehicleService.updateStatus(id, status);
        return ApiResponse.success("更新成功", null);
    }
}
