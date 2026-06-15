package com.leoyoung.backend.dto;

import java.math.BigDecimal;

/**
 * 购物车列表项响应 DTO
 * 合并了 cart_item 和 product 的数据，前端直接展示
 */
public class CartItemResponse {

    private Long cartItemId;
    private Long productId;
    private String productName;
    private String productImageUrl;
    private BigDecimal price;       // 商品单价
    private Integer quantity;       // 数量
    private String sweetness;
    private String temperature;
    private String cupSize;
    private String addOns;
    private BigDecimal subtotal;    // 小计 = price * quantity

    // ===== Getter / Setter =====
    public Long getCartItemId() { return cartItemId; }
    public void setCartItemId(Long cartItemId) { this.cartItemId = cartItemId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getSweetness() { return sweetness; }
    public void setSweetness(String sweetness) { this.sweetness = sweetness; }

    public String getTemperature() { return temperature; }
    public void setTemperature(String temperature) { this.temperature = temperature; }

    public String getCupSize() { return cupSize; }
    public void setCupSize(String cupSize) { this.cupSize = cupSize; }

    public String getAddOns() { return addOns; }
    public void setAddOns(String addOns) { this.addOns = addOns; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
