package com.fmjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.good.BrandDao;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.good.Brand;
import com.fmjava.core.pojo.good.BrandQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@Service
public class BrandServiceImpl implements BrandService {

    /**
     * 导入dao
     */
    @Autowired
    private BrandDao brandDao;

    // 分页查询品牌
    @Override
    public PageResult findAllBrands(Integer page,Integer rows,Brand brand) {
        /**
         *  分页查询品牌
         */
        PageHelper.startPage(page,rows);
        BrandQuery brandQuery = new BrandQuery();
        // 按照id降序排序
        brandQuery.setOrderByClause("id desc");
        // 匹配关键词搜索
        if (brand != null) {
            BrandQuery.Criteria criteria = brandQuery.createCriteria();
            if (brand.getName() != null && !"".equals(brand.getName())) {
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if (brand.getFirstChar() != null && !"".equals(brand.getFirstChar())) {
                criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");
            }
        }

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

    /**
     * 根据id数组删除品牌
     * @param idx
     */
    @Override
    public void delete(Long[] idx) {

        // 指定条件删除
        BrandQuery brandQuery = new BrandQuery();

        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        criteria.andIdIn(Arrays.asList(idx));

        brandDao.deleteByExample(brandQuery);

        // 遍历id删除
        /*for (Long aLong : idx) {
            brandDao.deleteByPrimaryKey(aLong);
        }*/
    }
}
