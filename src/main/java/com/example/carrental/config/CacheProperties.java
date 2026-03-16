package com.example.carrental.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

    private long bannerTtlMinutes;
    private long hotVehicleTtlMinutes;
    private long nullTtlMinutes;
    private long ttlRandomBoundMinutes;
}
