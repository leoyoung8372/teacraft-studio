package com.leoyoung.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 购物车实体 —— 映射 cart_item 表
 * 每个用户可添加多种商品到购物车，每种商品一条记录
 */
@Data
@TableName("cart_item")
public class CartItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;        // 所属用户 ID

    @TableField("product_id")
    private Long productId;     // 商品 ID

    private Integer quantity;   // 数量，默认 1

    private String sweetness;   // 甜度（全糖/七分糖/半糖/三分糖/无糖）

    private String temperature; // 温度（热/温/常温/去冰/少冰）

    @TableField("cup_size")
    private String cupSize;     // 杯型（中杯/大杯）

    @TableField("add_ons")
    private String addOns;      // 加料（珍珠/椰果/奶盖等，逗号分隔）

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
