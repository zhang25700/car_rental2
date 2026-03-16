package com.example.carrental.service.impl;

import com.example.carrental.config.CacheProperties;
import com.example.carrental.mapper.VehicleMapper;
import com.example.carrental.model.dto.VehicleQueryRequest;
import com.example.carrental.model.entity.Vehicle;
import com.example.carrental.model.vo.PageResult;
import com.example.carrental.service.VehicleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private static final String HOT_VEHICLE_CACHE_KEY = "cache:vehicle:hot";
    private static final String EMPTY_FLAG = "EMPTY";

    private final VehicleMapper vehicleMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheProperties cacheProperties;

    @Override
    public PageResult<Vehicle> page(VehicleQueryRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<Vehicle> list = vehicleMapper.selectByCondition(request);
        return PageResult.from(new PageInfo<>(list));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Vehicle> listHotVehicles() {
        Object cached = redisTemplate.opsForValue().get(HOT_VEHICLE_CACHE_KEY);
        if (cached instanceof List<?>) {
            return (List<Vehicle>) cached;
        }
        if (EMPTY_FLAG.equals(cached)) {
            return Collections.emptyList();
        }

        List<Vehicle> list = vehicleMapper.selectHotVehicles();
        if (list == null || list.isEmpty()) {
            redisTemplate.opsForValue().set(HOT_VEHICLE_CACHE_KEY, EMPTY_FLAG, Duration.ofMinutes(cacheProperties.getNullTtlMinutes()));
            return Collections.emptyList();
        }

        redisTemplate.opsForValue().set(HOT_VEHICLE_CACHE_KEY, list, randomTtl(cacheProperties.getHotVehicleTtlMinutes()));
        return list;
    }

    @Override
    public void save(Vehicle vehicle) {
        if (vehicle.getId() == null) {
            vehicleMapper.insert(vehicle);
        } else {
            vehicleMapper.update(vehicle);
        }
        redisTemplate.delete(HOT_VEHICLE_CACHE_KEY);
    }

    @Override
    public void updateStatus(Long id, String status) {
        vehicleMapper.updateStatus(id, status);
        redisTemplate.delete(HOT_VEHICLE_CACHE_KEY);
    }

    private Duration randomTtl(long baseMinutes) {
        long extra = ThreadLocalRandom.current().nextLong(cacheProperties.getTtlRandomBoundMinutes() + 1);
        return Duration.ofMinutes(baseMinutes + extra);
    }
}
