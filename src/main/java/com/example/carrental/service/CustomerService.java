package com.example.carrental.service;

import com.example.carrental.model.entity.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> list();

    void save(Customer customer);
}
