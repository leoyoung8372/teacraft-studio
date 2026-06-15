package com.leoyoung.backend.controller;

import com.leoyoung.backend.common.Result;
import com.leoyoung.backend.dto.LoginRequest;
import com.leoyoung.backend.dto.RegisterRequest;
import com.leoyoung.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户 Controller —— 处理 C 端用户注册和登录请求
 * Controller 只负责接收参数、调用 Service、返回 Result，不包含业务逻辑
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     * @param request 昵称、手机号、密码（@Valid 自动校验空值）
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return Result.success(null, "注册成功");
    }

    /**
     * 用户登录
     * @param request 手机号、密码
     */
    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(userService.login(request)); // 成功则返回 LoginResponse
    }
}
