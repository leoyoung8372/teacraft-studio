package com.leoyoung.backend.dto;

/**
 * 地址新增/编辑请求 DTO
 * 有 id 为编辑，无 id 为新增（复用表单）
 */
public class AddressSaveRequest {

    private Long id;
    private String receiverName;
    private String receiverPhone;
    private String detailAddress;
    private Integer isDefault;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getReceiverPhone() { return receiverPhone; }
    public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }
    public String getDetailAddress() { return detailAddress; }
    public void setDetailAddress(String detailAddress) { this.detailAddress = detailAddress; }
    public Integer getIsDefault() { return isDefault; }
    public void setIsDefault(Integer isDefault) { this.isDefault = isDefault; }
}
