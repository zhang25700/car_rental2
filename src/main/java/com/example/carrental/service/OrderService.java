package com.example.carrental.service;

import com.example.carrental.model.dto.OrderQueryRequest;
import com.example.carrental.model.entity.RentalOrder;
import com.example.carrental.model.vo.PageResult;

public interface OrderService {

    PageResult<RentalOrder> page(OrderQueryRequest request);

    void create(RentalOrder order);

    void updateStatus(Long id, String orderStatus);
}
