package com.fmjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.good.BrandDao;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.good.Brand;
import com.fmjava.core.pojo.good.BrandQuery;
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
        BrandQuery brandQuery = new BrandQuery();
        // 按照id降序排序
        brandQuery.setOrderByClause("id desc");
        Page<Brand> brandPageInfo = (Page<Brand>) brandDao.selectByExample(brandQuery);
        return new PageResult(brandPageInfo.getTotal(),brandPageInfo.getResult());
    }

    // 添加品牌
    @Override
    public void add(Brand brand) {
        brandDao.insertSelective(brand);
    }

    // 根据id查询单个品牌
    @Override
    public Brand findBrandWidthId(Long id) {
        Brand brand = brandDao.selectByPrimaryKey(id);
        return brand;
    }

    // 更新品牌
    @Override
    public void updata(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }
}
