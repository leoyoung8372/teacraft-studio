package com.leoyoung.backend.controller;

import com.leoyoung.backend.common.Result;
import com.leoyoung.backend.dto.AdminLoginRequest;
import com.leoyoung.backend.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员 Controller —— 处理商家端登录
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody AdminLoginRequest request) {
        return Result.success(adminService.login(request));
    }
}
