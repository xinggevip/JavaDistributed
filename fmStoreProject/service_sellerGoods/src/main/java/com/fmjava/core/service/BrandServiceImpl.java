package com.fmjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.good.BrandDao;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.good.Brand;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class BrandServiceImpl implements BrandService {

    /**
     * 导入dao
     */
    @Autowired
    private BrandDao brandDao;

    // 分页查询品牌
    @Override
    public PageResult findAllBrands(Integer page,Integer rows) {
        /**
         *  分页查询品牌
         */
        PageHelper.startPage(page,rows);
        Page<Brand> brandPageInfo = (Page<Brand>) brandDao.selectByExample(null);
        return new PageResult(brandPageInfo.getTotal(),brandPageInfo.getResult());
    }

    // 添加品牌
    @Override
    public void add(Brand brand) {
        brandDao.insertSelective(brand);
    }
}
