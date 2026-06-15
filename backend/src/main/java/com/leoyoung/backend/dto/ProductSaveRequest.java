package com.leoyoung.backend.dto;

import java.math.BigDecimal;

/**
 * 商品新增/编辑请求 DTO
 * 有 id 字段时为更新，无 id 时为新增（复用一个表单，符合文档简化设计）
 */
public class ProductSaveRequest {

    private Long id;                // 更新时传，新增时不传
    private Long categoryId;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private Integer stock;
    private Integer isOnSale;

    // ========== Getter / Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getIsOnSale() { return isOnSale; }
    public void setIsOnSale(Integer isOnSale) { this.isOnSale = isOnSale; }
}
