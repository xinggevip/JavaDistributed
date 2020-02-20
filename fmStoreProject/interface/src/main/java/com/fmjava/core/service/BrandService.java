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

    /**
     * 根据id查单个品牌
     * @param id
     * @return
     */
    Brand findBrandWidthId(Long id);

    /**
     * 更新品牌
     * @param brand
     */
    void updata(Brand brand);

    /**
     * 根据id数组删除品牌
     * @param idx
     */
    void delete(Long[] idx);
}
