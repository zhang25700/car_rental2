package com.example.carrental;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.carrental.mapper")
/**
 * Spring Boot 启动入口。
 * 同时开启 MyBatis Mapper 扫描，加载整个汽车租赁系统的后端能力。
 */
public class CarRentalApplication {

    /**
     * 应用启动方法。
     */
    public static void main(String[] args) {
        SpringApplication.run(CarRentalApplication.class, args);
    }
}
