package com.example.carrental.controller;

import com.example.carrental.common.ApiResponse;
import com.example.carrental.model.dto.OrderQueryRequest;
import com.example.carrental.model.entity.RentalOrder;
import com.example.carrental.model.vo.PageResult;
import com.example.carrental.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ApiResponse<PageResult<RentalOrder>> page(OrderQueryRequest request) {
        return ApiResponse.success(orderService.page(request));
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody RentalOrder order) {
        orderService.create(order);
        return ApiResponse.success("created", null);
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        orderService.updateStatus(id, status);
        return ApiResponse.success("updated", null);
    }
}
