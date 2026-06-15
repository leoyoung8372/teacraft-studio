package com.leoyoung.backend.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置
 * 注册分页插件 + 扫描 Mapper 接口
 */
@Configuration
@MapperScan("com.leoyoung.backend.mapper") // 扫描所有 Mapper 接口，省去每个接口上加 @Mapper
public class MyBatisPlusConfig {

    /**
     * MyBatis Plus 拦截器链 —— 目前只注册分页插件
     * 后续商品列表、订单管理查询都会用到分页
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
