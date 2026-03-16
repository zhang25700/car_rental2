package com.example.carrental.mapper;

import com.example.carrental.model.entity.Banner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BannerMapper {

    List<Banner> selectActiveBanners();

    int insert(Banner banner);

    int update(Banner banner);

    Banner selectById(@Param("id") Long id);
}
