package com.example.carrental.controller;

import com.example.carrental.common.ApiResponse;
import com.example.carrental.model.entity.Customer;
import com.example.carrental.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
/**
 * 客户档案管理接口。
 * 负责后台客户资料查询和维护。
 */
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 查询客户列表。
     */
    @GetMapping
    public ApiResponse<List<Customer>> list() {
        return ApiResponse.success(customerService.list());
    }

    /**
     * 新增或修改客户资料。
     */
    @PostMapping
    public ApiResponse<Void> save(@RequestBody Customer customer) {
        customerService.save(customer);
        return ApiResponse.success("保存成功", null);
    }
}
