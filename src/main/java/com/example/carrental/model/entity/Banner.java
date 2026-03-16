package com.example.carrental.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Banner {

    private Long id;
    private String title;
    private String imageUrl;
    private String redirectUrl;
    private Integer sort;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
