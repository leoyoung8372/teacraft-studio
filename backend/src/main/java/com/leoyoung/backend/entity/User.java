package com.leoyoung.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * C 端用户实体 —— 映射 user 表
 * 字段名称、类型与 database.sql 完全一致，不增不减
 */
@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)    // 自增主键
    private Long id;

    private String nickname;        // 昵称

    private String phone;           // 手机号（登录账号）

    private String password;        // 密码（密文存储）

    @TableField("avatar_url")
    private String avatarUrl;       // 头像URL

    @TableField("created_at")
    private LocalDateTime createdAt; // 创建时间

    @TableField("updated_at")
    private LocalDateTime updatedAt; // 更新时间
}
