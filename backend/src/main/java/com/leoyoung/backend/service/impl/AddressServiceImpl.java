package com.leoyoung.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.leoyoung.backend.dto.AddressSaveRequest;
import com.leoyoung.backend.entity.Address;
import com.leoyoung.backend.mapper.AddressMapper;
import com.leoyoung.backend.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地址服务实现
 */
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    @Override
    public List<Address> list(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
               .orderByDesc(Address::getIsDefault)   // 默认地址排最前
               .orderByDesc(Address::getCreatedAt);
        return addressMapper.selectList(wrapper);
    }

    @Override
    public void save(Long userId, AddressSaveRequest request) {
        // 设为默认时，先将该用户其他地址取消默认
        if (request.getIsDefault() != null && request.getIsDefault() == 1) {
            LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Address::getUserId, userId)
                   .eq(Address::getIsDefault, 1);
            Address oldDefault = addressMapper.selectOne(wrapper);
            if (oldDefault != null) {
                oldDefault.setIsDefault(0);
                addressMapper.updateById(oldDefault);
            }
        }

        Address addr = new Address();
        addr.setReceiverName(request.getReceiverName());
        addr.setReceiverPhone(request.getReceiverPhone());
        addr.setDetailAddress(request.getDetailAddress());
        addr.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : 0);

        if (request.getId() != null) {
            addr.setId(request.getId());
            addressMapper.updateById(addr); // 编辑
        } else {
            addr.setUserId(userId);
            addressMapper.insert(addr);      // 新增
        }
    }

    @Override
    public void delete(Long id) {
        addressMapper.deleteById(id);
    }
}
