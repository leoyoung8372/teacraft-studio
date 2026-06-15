package com.leoyoung.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leoyoung.backend.dto.ProductSaveRequest;
import com.leoyoung.backend.entity.Product;
import com.leoyoung.backend.mapper.ProductMapper;
import com.leoyoung.backend.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 商品服务实现
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public List<Product> listByCategory(Long categoryId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCategoryId, categoryId)  // 指定分类
               .eq(Product::getIsOnSale, 1)              // 仅已上架
               .orderByDesc(Product::getCreatedAt);      // 最新在前
        return productMapper.selectList(wrapper);
    }

    @Override
    public Page<Product> listAll(String keyword, int page, int size) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        // 关键词不为空时按商品名模糊搜索
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Product::getName, keyword);
        }
        wrapper.orderByDesc(Product::getCreatedAt);
        return productMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public void save(ProductSaveRequest request) {
        Product product = new Product();
        product.setCategoryId(request.getCategoryId());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setIsOnSale(request.getIsOnSale());

        if (request.getId() != null) {
            // 有 id → 更新
            product.setId(request.getId());
            productMapper.updateById(product);
        } else {
            // 无 id → 新增
            productMapper.insert(product);
        }
    }

    @Override
    public void delete(Long id) {
        productMapper.deleteById(id);
    }
}
