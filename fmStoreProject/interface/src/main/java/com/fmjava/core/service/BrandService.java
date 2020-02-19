package com.fmjava.core.service;

import com.fmjava.core.pojo.entity.PageResult;

public interface BrandService {
    // 分页查询品牌
    PageResult findAllBrands(Integer page,Integer rows);
}
