package com.example.carrental.service;

import com.example.carrental.model.dto.UserSaveRequest;
import com.example.carrental.model.vo.UserVO;

import java.util.List;

public interface UserService {

    List<UserVO> list();

    void save(UserSaveRequest request);
}
