package com.example.carrental.service.impl;

import com.example.carrental.config.CacheProperties;
import com.example.carrental.mapper.BannerMapper;
import com.example.carrental.model.entity.Banner;
import com.example.carrental.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private static final String BANNER_CACHE_KEY = "cache:banner:list";
    private static final String EMPTY_FLAG = "EMPTY";

    private final BannerMapper bannerMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheProperties cacheProperties;

    @Override
    @SuppressWarnings("unchecked")
    public List<Banner> listActiveBanners() {
        Object cached = redisTemplate.opsForValue().get(BANNER_CACHE_KEY);
        if (cached instanceof List<?>) {
            return (List<Banner>) cached;
        }
        if (EMPTY_FLAG.equals(cached)) {
            return Collections.emptyList();
        }

        List<Banner> banners = bannerMapper.selectActiveBanners();
        if (banners == null || banners.isEmpty()) {
            redisTemplate.opsForValue().set(BANNER_CACHE_KEY, EMPTY_FLAG, Duration.ofMinutes(cacheProperties.getNullTtlMinutes()));
            return Collections.emptyList();
        }
        redisTemplate.opsForValue().set(BANNER_CACHE_KEY, banners, randomTtl(cacheProperties.getBannerTtlMinutes()));
        return banners;
    }

    @Override
    public void save(Banner banner) {
        if (banner.getId() == null) {
            bannerMapper.insert(banner);
        } else {
            bannerMapper.update(banner);
        }
        redisTemplate.delete(BANNER_CACHE_KEY);
    }

    private Duration randomTtl(long baseMinutes) {
        long extra = ThreadLocalRandom.current().nextLong(cacheProperties.getTtlRandomBoundMinutes() + 1);
        return Duration.ofMinutes(baseMinutes + extra);
    }
}
