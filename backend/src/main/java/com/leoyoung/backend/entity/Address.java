package com.leoyoung.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 地址实体 —— 映射 address 表
 */
@Data
@TableName("address")
public class Address {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("receiver_name")
    private String receiverName;    // 收货人姓名

    @TableField("receiver_phone")
    private String receiverPhone;   // 收货人手机号

    @TableField("detail_address")
    private String detailAddress;   // 详细地址

    @TableField("is_default")
    private Integer isDefault;      // 是否默认：0-否，1-是

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
