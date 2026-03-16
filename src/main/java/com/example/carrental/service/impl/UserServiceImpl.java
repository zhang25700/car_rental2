package com.example.carrental.service.impl;

import com.example.carrental.common.BusinessException;
import com.example.carrental.common.ErrorCode;
import com.example.carrental.mapper.SysUserMapper;
import com.example.carrental.model.dto.UserSaveRequest;
import com.example.carrental.model.entity.SysUser;
import com.example.carrental.model.vo.UserVO;
import com.example.carrental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SysUserMapper sysUserMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<UserVO> list() {
        return sysUserMapper.selectAll().stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public void save(UserSaveRequest request) {
        if (request.getId() == null) {
            if (sysUserMapper.selectByUsername(request.getUsername()) != null) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "用户名已存在");
            }
            SysUser user = new SysUser();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(defaultPassword(request.getPassword())));
            user.setRealName(request.getRealName());
            user.setRole(request.getRole());
            user.setStatus(request.getStatus());
            sysUserMapper.insert(user);
            return;
        }

        SysUser existing = sysUserMapper.selectById(request.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        SysUser byUsername = sysUserMapper.selectByUsername(request.getUsername());
        if (byUsername != null && !byUsername.getId().equals(request.getId())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "用户名已存在");
        }

        existing.setUsername(request.getUsername());
        existing.setRealName(request.getRealName());
        existing.setRole(request.getRole());
        existing.setStatus(request.getStatus());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        sysUserMapper.update(existing);
    }

    private String defaultPassword(String password) {
        return password == null || password.isBlank() ? "password" : password;
    }

    private UserVO toVO(SysUser user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
