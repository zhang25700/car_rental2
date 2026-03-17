package com.example.carrental.controller;

import com.example.carrental.common.ApiResponse;
import com.example.carrental.model.dto.UserRentOrderRequest;
import com.example.carrental.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/orders")
@RequiredArgsConstructor
/**
 * 用户端订单接口。
 * 当前主要提供“立即租车”的下单入口。
 */
public class UserOrderController {

    private final OrderService orderService;

    /**
     * 用户提交租车订单。
     */
    @PostMapping("/rent")
    public ApiResponse<Void> rent(@Valid @RequestBody UserRentOrderRequest request) {
        orderService.createUserOrder(request);
        return ApiResponse.success("下单成功", null);
    }
}
