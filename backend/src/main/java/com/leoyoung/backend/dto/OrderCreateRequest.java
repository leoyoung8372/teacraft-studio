package com.leoyoung.backend.dto;

/**
 * 创建订单请求 DTO
 */
public class OrderCreateRequest {

    /** 用餐方式：self_pickup / delivery */
    private String mealType;

    /** 收货地址 ID（外卖时必传） */
    private Long addressId;

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }
}
