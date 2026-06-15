package com.leoyoung.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leoyoung.backend.dto.ProductSaveRequest;
import com.leoyoung.backend.entity.Product;

import java.util.List;

/**
 * 商品服务接口
 */
public interface ProductService {

    /** 用户端：按分类查询已上架商品 */
    List<Product> listByCategory(Long categoryId);

    /** 用户端：查询单个商品详情 */
    Product getById(Long id);

    /** 商家端：分页 + 关键词搜索商品 */
    Page<Product> listAll(String keyword, int page, int size);

    /** 商家端：新增或更新商品（有 id 则更新，无 id 则新增） */
    void save(ProductSaveRequest request);

    /** 商家端：删除商品 */
    void delete(Long id);
}
