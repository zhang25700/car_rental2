package com.example.carrental.service.impl;

import com.example.carrental.context.UserContext;
import com.example.carrental.mapper.RentalOrderMapper;
import com.example.carrental.mapper.VehicleMapper;
import com.example.carrental.model.dto.OrderQueryRequest;
import com.example.carrental.model.entity.RentalOrder;
import com.example.carrental.model.vo.PageResult;
import com.example.carrental.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final RentalOrderMapper rentalOrderMapper;
    private final VehicleMapper vehicleMapper;

    @Override
    public PageResult<RentalOrder> page(OrderQueryRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<RentalOrder> list = rentalOrderMapper.selectByCondition(request);
        return PageResult.from(new PageInfo<>(list));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(RentalOrder order) {
        order.setOrderNo(buildOrderNo());
        order.setCreatedBy(UserContext.getUserId());
        order.setOrderStatus(order.getOrderStatus() == null ? "CREATED" : order.getOrderStatus());
        rentalOrderMapper.insert(order);
        if (order.getVehicleId() != null) {
            vehicleMapper.updateStatus(order.getVehicleId(), "RENTED");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, String orderStatus) {
        rentalOrderMapper.updateStatus(id, orderStatus);
    }

    private String buildOrderNo() {
        return "CR" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
