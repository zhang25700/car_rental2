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
/**
 * 后台订单管理接口。
 * 负责订单查询、人工建单和订单状态维护。
 */
public class OrderController {

    private final OrderService orderService;

    /**
     * 分页查询订单。
     */
    @GetMapping
    public ApiResponse<PageResult<RentalOrder>> page(OrderQueryRequest request) {
        return ApiResponse.success(orderService.page(request));
    }

    /**
     * 后台创建订单。
     */
    @PostMapping
    public ApiResponse<Void> create(@RequestBody RentalOrder order) {
        orderService.create(order);
        return ApiResponse.success("创建成功", null);
    }

    /**
     * 修改订单状态。
     */
    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        orderService.updateStatus(id, status);
        return ApiResponse.success("更新成功", null);
    }
}
