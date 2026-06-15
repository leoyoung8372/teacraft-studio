package com.leoyoung.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单主表实体 —— 映射 order_info 表
 */
@Data
@TableName("order_info")
public class OrderInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("order_no")
    private String orderNo;         // 订单号

    @TableField("user_id")
    private Long userId;            // 用户 ID

    @TableField("total_amount")
    private BigDecimal totalAmount; // 订单总价

    @TableField("meal_type")
    private String mealType;        // 用餐方式：self_pickup / delivery

    @TableField("address_id")
    private Long addressId;         // 收货地址 ID（外卖时使用）

    private String status;          // 状态：making / finished / cancelled

    @TableField("paid_at")
    private LocalDateTime paidAt;   // 支付时间

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
