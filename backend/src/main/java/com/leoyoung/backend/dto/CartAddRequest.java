package com.leoyoung.backend.dto;

/**
 * 加入购物车请求 DTO
 */
public class CartAddRequest {

    private Long productId;
    private Integer quantity;
    private String sweetness;
    private String temperature;
    private String cupSize;
    private String addOns;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

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
}
