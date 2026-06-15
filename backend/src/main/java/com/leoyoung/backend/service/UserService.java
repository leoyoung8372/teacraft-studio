package com.leoyoung.backend.service;

import com.leoyoung.backend.dto.LoginResponse;
import com.leoyoung.backend.dto.RegisterRequest;
import com.leoyoung.backend.dto.LoginRequest;

/**
 * 用户服务接口 —— 定义 C 端用户核心操作
 */
public interface UserService {

    /**
     * 用户注册
     * @param request 昵称、手机号、密码
     */
    void register(RegisterRequest request);

    /**
     * 用户登录
     * @param request 手机号、密码
     * @return 登录响应（Token + 用户基本信息）
     */
    LoginResponse login(LoginRequest request);
}
