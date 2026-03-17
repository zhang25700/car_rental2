package com.example.carrental.service.impl;

import com.example.carrental.mapper.CustomerMapper;
import com.example.carrental.model.entity.Customer;
import com.example.carrental.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
/**
 * 客户服务实现。
 * 负责后台客户档案的查询与维护。
 */
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;

    /**
     * 查询全部客户列表。
     */
    @Override
    public List<Customer> list() {
        return customerMapper.selectAll();
    }

    /**
     * 新增或修改客户资料。
     */
    @Override
    public void save(Customer customer) {
        if (customer.getId() == null) {
            customerMapper.insert(customer);
        } else {
            customerMapper.update(customer);
        }
    }
}
