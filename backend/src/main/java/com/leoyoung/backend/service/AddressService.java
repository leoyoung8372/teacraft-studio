package com.leoyoung.backend.service;

import com.leoyoung.backend.dto.AddressSaveRequest;
import com.leoyoung.backend.entity.Address;

import java.util.List;

/**
 * 地址服务接口
 */
public interface AddressService {

    /** 查询用户所有地址 */
    List<Address> list(Long userId);

    /** 新增或编辑地址 */
    void save(Long userId, AddressSaveRequest request);

    /** 删除地址 */
    void delete(Long id);
}
