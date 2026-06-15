package com.leoyoung.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leoyoung.backend.entity.User;

/**
 * 用户 Mapper —— 继承 MyBatis Plus BaseMapper，自动获得 CRUD 方法
 * 无需手写 SQL，复杂查询在 Service 层用 LambdaQueryWrapper 构建
 */
public interface UserMapper extends BaseMapper<User> {
}
