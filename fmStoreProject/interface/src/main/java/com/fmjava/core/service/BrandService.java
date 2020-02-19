package com.fmjava.core.service;

import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.good.Brand;

public interface BrandService {
    // 分页查询品牌
    PageResult findAllBrands(Integer page,Integer rows);

    /**
     * 添加品牌
     * @param brand
     */
    void add(Brand brand);
}
