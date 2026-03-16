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
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ApiResponse<List<Customer>> list() {
        return ApiResponse.success(customerService.list());
    }

    @PostMapping
    public ApiResponse<Void> save(@RequestBody Customer customer) {
        customerService.save(customer);
        return ApiResponse.success("saved", null);
    }
}
