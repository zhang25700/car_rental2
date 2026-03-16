package com.example.carrental.mapper;

import com.example.carrental.model.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerMapper {

    List<Customer> selectAll();

    Customer selectById(@Param("id") Long id);

    Customer selectByPhone(@Param("phone") String phone);

    int insert(Customer customer);

    int update(Customer customer);
}
