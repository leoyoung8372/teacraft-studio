package com.leoyoung.backend.service;

import com.leoyoung.backend.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {

    /** 获取所有分类列表（按 sort_order 排序） */
    List<Category> list();
}
