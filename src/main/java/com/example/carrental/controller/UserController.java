package com.example.carrental.controller;

import com.example.carrental.common.ApiResponse;
import com.example.carrental.model.dto.UserSaveRequest;
import com.example.carrental.model.vo.UserVO;
import com.example.carrental.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
/**
 * 后台用户账号管理接口。
 * 负责账号查询、新增、编辑和密码重置入口。
 */
public class UserController {

    private final UserService userService;

    /**
     * 查询系统账号列表。
     */
    @GetMapping
    public ApiResponse<List<UserVO>> list() {
        return ApiResponse.success(userService.list());
    }

    /**
     * 新增或修改用户账号。
     */
    @PostMapping
    public ApiResponse<Void> save(@Valid @RequestBody UserSaveRequest request) {
        userService.save(request);
        return ApiResponse.success("保存成功", null);
    }
}
