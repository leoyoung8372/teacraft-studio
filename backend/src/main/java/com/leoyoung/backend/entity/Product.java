package com.leoyoung.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体 —— 映射 product 表
 */
@Data
@TableName("product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("category_id")
    private Long categoryId;        // 所属分类ID

    private String name;            // 商品名称

    private String description;     // 商品简介

    @TableField("image_url")
    private String imageUrl;        // 商品图片URL

    private BigDecimal price;       // 商品价格

    private Integer stock;          // 库存数量

    @TableField("is_on_sale")
    private Integer isOnSale;       // 是否上架：0-否，1-是

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
