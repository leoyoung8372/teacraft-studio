package com.leoyoung.backend.controller;

import com.leoyoung.backend.common.Result;
import com.leoyoung.backend.dto.AddressSaveRequest;
import com.leoyoung.backend.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 地址 Controller —— 处理用户收货地址的增删改查
 */
@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /** 获取用户所有地址 */
    @GetMapping("/list")
    public Result<?> list(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return Result.success(addressService.list(userId));
    }

    /** 新增/编辑地址 */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody AddressSaveRequest request, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        addressService.save(userId, request);
        return Result.success(null, request.getId() != null ? "修改成功" : "已添加");
    }

    /** 删除地址 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return Result.success(null, "已删除");
    }
}
