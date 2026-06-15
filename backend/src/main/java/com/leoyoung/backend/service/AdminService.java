package com.leoyoung.backend.service;

import com.leoyoung.backend.dto.AdminLoginRequest;
import com.leoyoung.backend.dto.AdminLoginResponse;

/**
 * 管理员服务接口 —— 仅提供登录，不提供注册
 */
public interface AdminService {

    /**
     * 管理员登录
     * @param request 用户名、密码
     * @return 登录响应（Token + 管理员基本信息）
     */
    AdminLoginResponse login(AdminLoginRequest request);
}
