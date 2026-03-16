package com.example.carrental.mapper;

import com.example.carrental.model.dto.OrderQueryRequest;
import com.example.carrental.model.entity.RentalOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RentalOrderMapper {

    List<RentalOrder> selectByCondition(OrderQueryRequest request);

    RentalOrder selectById(@Param("id") Long id);

    int insert(RentalOrder order);

    int updateStatus(@Param("id") Long id, @Param("orderStatus") String orderStatus);
}
