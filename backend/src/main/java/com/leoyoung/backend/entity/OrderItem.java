package com.leoyoung.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细实体 —— 映射 order_item 表
 * 保存下单时的商品快照，避免后续商品修改影响历史订单
 */
@Data
@TableName("order_item")
public class OrderItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("order_id")
    private Long orderId;           // 所属订单 ID

    @TableField("product_id")
    private Long productId;         // 商品 ID

    @TableField("product_name")
    private String productName;     // 商品名称快照

    @TableField("product_image_url")
    private String productImageUrl; // 商品图片快照

    private String sweetness;       // 甜度快照
    private String temperature;     // 温度快照

    @TableField("cup_size")
    private String cupSize;         // 杯型快照

    @TableField("add_ons")
    private String addOns;          // 加料快照

    private BigDecimal price;       // 单价快照

    private Integer quantity;       // 数量

    private BigDecimal subtotal;    // 小计

    @TableField("created_at")
    private LocalDateTime createdAt;
}
