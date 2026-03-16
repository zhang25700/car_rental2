package com.example.carrental.controller;

import com.example.carrental.common.ApiResponse;
import com.example.carrental.model.entity.Banner;
import com.example.carrental.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping("/active")
    public ApiResponse<List<Banner>> listActiveBanners() {
        return ApiResponse.success(bannerService.listActiveBanners());
    }

    @PostMapping
    public ApiResponse<Void> save(@RequestBody Banner banner) {
        bannerService.save(banner);
        return ApiResponse.success("saved", null);
    }
}
