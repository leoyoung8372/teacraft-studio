package com.leoyoung.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.leoyoung.backend.common.ResultCode;
import com.leoyoung.backend.common.exception.BusinessException;
import com.leoyoung.backend.dto.AdminLoginRequest;
import com.leoyoung.backend.dto.AdminLoginResponse;
import com.leoyoung.backend.entity.Admin;
import com.leoyoung.backend.mapper.AdminMapper;
import com.leoyoung.backend.service.AdminService;
import com.leoyoung.backend.util.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 管理员服务实现 —— 处理管理员登录的核心业务逻辑
 */
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;
    private final JwtUtils jwtUtils;

    public AdminServiceImpl(AdminMapper adminMapper, JwtUtils jwtUtils) {
        this.adminMapper = adminMapper;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public AdminLoginResponse login(AdminLoginRequest request) {
        // 1. 根据用户名查询管理员
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, request.getUsername());
        Admin admin = adminMapper.selectOne(wrapper);
        if (admin == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND, "管理员不存在");
        }

        // 2. MD5 加密输入密码并与数据库密文对比
        String encryptedInput = DigestUtils.md5DigestAsHex(
                request.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!encryptedInput.equals(admin.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        // 3. 签发 JWT（userType="admin"，区分商家端）
        String token = jwtUtils.generateToken(admin.getId(), "admin");

        // 4. 组装登录响应
        AdminLoginResponse response = new AdminLoginResponse();
        response.setToken(token);
        response.setAdminId(admin.getId());
        response.setUsername(admin.getUsername());
        return response;
    }
}
