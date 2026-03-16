package com.example.carrental.mapper;

import com.example.carrental.model.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserMapper {

    SysUser selectByUsername(@Param("username") String username);

    java.util.List<SysUser> selectAll();

    SysUser selectById(@Param("id") Long id);

    int insert(SysUser user);

    int update(SysUser user);
}
