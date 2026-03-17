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
/**
 * 广告位管理接口。
 * 提供用户端广告查询和后台广告维护能力。
 */
public class BannerController {

    private final BannerService bannerService;

    /**
     * 查询当前启用的广告列表。
     */
    @GetMapping("/active")
    public ApiResponse<List<Banner>> listActiveBanners() {
        return ApiResponse.success(bannerService.listActiveBanners());
    }

    /**
     * 新增或修改广告。
     */
    @PostMapping
    public ApiResponse<Void> save(@RequestBody Banner banner) {
        bannerService.save(banner);
        return ApiResponse.success("保存成功", null);
    }
}
