package com.example.carrental.service.impl;

import com.example.carrental.mapper.CustomerMapper;
import com.example.carrental.model.entity.Customer;
import com.example.carrental.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;

    @Override
    public List<Customer> list() {
        return customerMapper.selectAll();
    }

    @Override
    public void save(Customer customer) {
        if (customer.getId() == null) {
            customerMapper.insert(customer);
        } else {
            customerMapper.update(customer);
        }
    }
}
