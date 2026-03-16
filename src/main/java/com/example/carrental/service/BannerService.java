package com.example.carrental.service;

import com.example.carrental.model.entity.Banner;

import java.util.List;

public interface BannerService {

    List<Banner> listActiveBanners();

    void save(Banner banner);
}
