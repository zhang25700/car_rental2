package com.example.carrental.mapper;

import com.example.carrental.model.dto.VehicleQueryRequest;
import com.example.carrental.model.entity.Vehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VehicleMapper {

    List<Vehicle> selectByCondition(VehicleQueryRequest request);

    List<Vehicle> selectHotVehicles();

    Vehicle selectById(@Param("id") Long id);

    int insert(Vehicle vehicle);

    int update(Vehicle vehicle);

    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
