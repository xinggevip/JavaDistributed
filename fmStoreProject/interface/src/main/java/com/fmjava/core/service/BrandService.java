package com.fmjava.core.service;

import com.fmjava.core.pojo.good.Brand;

import java.util.List;

public interface BrandService {
    // 查所有的品牌
    List<Brand> findAllBrands();
}
