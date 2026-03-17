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
/**
 * 车辆服务实现。
 * 负责车辆分页查询、热点车辆缓存，以及车辆变更后的缓存失效处理。
 */
public class VehicleServiceImpl implements VehicleService {

    private static final String HOT_VEHICLE_CACHE_KEY = "cache:vehicle:hot";
    private static final String EMPTY_FLAG = "EMPTY";

    private final VehicleMapper vehicleMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheProperties cacheProperties;

    /**
     * 后台车辆分页查询，直接走数据库动态 SQL。
     */
    @Override
    public PageResult<Vehicle> page(VehicleQueryRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<Vehicle> list = vehicleMapper.selectByCondition(request);
        return PageResult.from(new PageInfo<>(list));
    }

    /**
     * 热点车辆采用 Cache-Aside 模式读取。
     * 先查 Redis，未命中再查数据库，并通过空值缓存和随机过期时间降低穿透与雪崩风险。
     */
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

    /**
     * 车辆新增或修改后，主动删除热点缓存，等待下次访问重新构建。
     */
    @Override
    public void save(Vehicle vehicle) {
        if (vehicle.getId() == null) {
            vehicleMapper.insert(vehicle);
        } else {
            vehicleMapper.update(vehicle);
        }
        redisTemplate.delete(HOT_VEHICLE_CACHE_KEY);
    }

    /**
     * 更新车辆状态并同步清理热点缓存。
     */
    @Override
    public void updateStatus(Long id, String status) {
        vehicleMapper.updateStatus(id, status);
        redisTemplate.delete(HOT_VEHICLE_CACHE_KEY);
    }

    /**
     * 在基础 TTL 上叠加随机分钟数，降低大量缓存 Key 同时过期带来的冲击。
     */
    private Duration randomTtl(long baseMinutes) {
        long extra = ThreadLocalRandom.current().nextLong(cacheProperties.getTtlRandomBoundMinutes() + 1);
        return Duration.ofMinutes(baseMinutes + extra);
    }
}
