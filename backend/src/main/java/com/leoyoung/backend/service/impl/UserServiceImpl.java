package com.leoyoung.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.leoyoung.backend.common.ResultCode;
import com.leoyoung.backend.common.exception.BusinessException;
import com.leoyoung.backend.dto.LoginRequest;
import com.leoyoung.backend.dto.LoginResponse;
import com.leoyoung.backend.dto.RegisterRequest;
import com.leoyoung.backend.entity.User;
import com.leoyoung.backend.mapper.UserMapper;
import com.leoyoung.backend.service.UserService;
import com.leoyoung.backend.util.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 用户服务实现 —— 处理注册和登录的核心业务逻辑
 * 业务校验放在 Service 层，Controller 只负责接收请求和返回 Result
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(UserMapper userMapper, JwtUtils jwtUtils) {
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void register(RegisterRequest request) {
        // 1. 检查手机号是否已注册
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, request.getPhone());
        User existingUser = userMapper.selectOne(wrapper);
        if (existingUser != null) {
            throw new BusinessException(ResultCode.USER_EXISTS);
        }

        // 2. 密码 MD5 加密
        String encryptedPassword = DigestUtils.md5DigestAsHex(
                request.getPassword().getBytes(StandardCharsets.UTF_8));

        // 3. 构造 User 实体并写入数据库
        User user = new User();
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setPassword(encryptedPassword);
        userMapper.insert(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. 根据手机号查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, request.getPhone());
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 2. 加密输入密码并与数据库密文对比
        String encryptedInput = DigestUtils.md5DigestAsHex(
                request.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!encryptedInput.equals(user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        // 3. 签发 JWT
        String token = jwtUtils.generateToken(user.getId(), "user");

        // 4. 组装登录响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setNickname(user.getNickname());
        response.setAvatarUrl(user.getAvatarUrl());
        return response;
    }
}
