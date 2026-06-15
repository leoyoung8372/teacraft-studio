-- TeaCraft Studio
-- MySQL 初始化脚本
-- 数据库名：teacraft_db

CREATE DATABASE IF NOT EXISTS teacraft_db
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE teacraft_db;

-- =========================
-- 1. 用户表
-- =========================
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    nickname VARCHAR(50) NOT NULL COMMENT '昵称',
    phone VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
    password VARCHAR(100) NOT NULL COMMENT '密码（建议后端加密存储）',
    avatar_url VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='C端用户表';

-- =========================
-- 2. 管理员表
-- =========================
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码（建议后端加密存储）',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家端管理员表';

-- =========================
-- 3. 地址表
-- =========================
CREATE TABLE IF NOT EXISTS address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '地址ID',
    user_id BIGINT NOT NULL COMMENT '所属用户ID',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人手机号',
    detail_address VARCHAR(255) NOT NULL COMMENT '详细地址',
    is_default TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认地址：0-否，1-是',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_address_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户地址表';

-- =========================
-- 4. 分类表
-- =========================
CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序值',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- =========================
-- 5. 商品表
-- =========================
CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    description VARCHAR(255) DEFAULT NULL COMMENT '商品简介',
    image_url VARCHAR(255) DEFAULT NULL COMMENT '商品图片URL',
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '商品价格',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    is_on_sale TINYINT NOT NULL DEFAULT 1 COMMENT '是否上架：0-否，1-是',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_product_category_id (category_id),
    INDEX idx_product_is_on_sale (is_on_sale)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- =========================
-- 6. 购物车表
-- =========================
CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车条目ID',
    user_id BIGINT NOT NULL COMMENT '所属用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    sweetness VARCHAR(20) DEFAULT NULL COMMENT '甜度',
    temperature VARCHAR(20) DEFAULT NULL COMMENT '温度',
    cup_size VARCHAR(20) DEFAULT NULL COMMENT '杯型',
    add_ons VARCHAR(255) DEFAULT NULL COMMENT '加料',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_cart_user_id (user_id),
    INDEX idx_cart_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- =========================
-- 7. 订单主表
-- =========================
CREATE TABLE IF NOT EXISTS order_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(64) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '所属用户ID',
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '订单总价',
    meal_type VARCHAR(20) NOT NULL COMMENT '用餐方式：self_pickup-自提，delivery-外卖',
    address_id BIGINT DEFAULT NULL COMMENT '收货地址ID（外卖时使用）',
    status VARCHAR(20) NOT NULL COMMENT '订单状态：making-制作中，finished-已完成，cancelled-已取消',
    paid_at DATETIME DEFAULT NULL COMMENT '支付时间（模拟）',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_user_id (user_id),
    INDEX idx_order_status (status),
    INDEX idx_order_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单主表';

-- =========================
-- 8. 订单明细表
-- =========================
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单明细ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(100) NOT NULL COMMENT '商品名称快照',
    product_image_url VARCHAR(255) DEFAULT NULL COMMENT '商品图片快照',
    sweetness VARCHAR(20) DEFAULT NULL COMMENT '甜度快照',
    temperature VARCHAR(20) DEFAULT NULL COMMENT '温度快照',
    cup_size VARCHAR(20) DEFAULT NULL COMMENT '杯型快照',
    add_ons VARCHAR(255) DEFAULT NULL COMMENT '加料快照',
    price DECIMAL(10,2) NOT NULL COMMENT '单价快照',
    quantity INT NOT NULL COMMENT '数量',
    subtotal DECIMAL(10,2) NOT NULL COMMENT '小计',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_item_order_id (order_id),
    INDEX idx_order_item_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- =========================
-- 初始化数据
-- =========================

-- 默认管理员
-- 开发阶段可直接使用明文初始密码，后续后端可改为加密校验
INSERT INTO admin (username, password)
VALUES ('admin', 'admin123');

-- 固定分类
INSERT INTO category (name, sort_order) VALUES
('新鲜冰淇淋', 1),
('提神咖啡', 2),
('清爽果茶', 3),
('经典奶茶', 4),
('其他', 5);