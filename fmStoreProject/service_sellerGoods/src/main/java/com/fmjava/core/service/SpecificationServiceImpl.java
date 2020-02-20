package com.fmjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.specification.SpecificationDao;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.specification.Specification;
import com.fmjava.core.pojo.specification.SpecificationQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecificationDao specDao;

    @Override
    public PageResult findPage(Specification spec, Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        SpecificationQuery specQuery = new SpecificationQuery();
        specQuery.setOrderByClause("id desc");
        if (spec != null){
            SpecificationQuery.Criteria criteria = specQuery.createCriteria();
            if (spec.getSpecName() !=null && !"".equals(spec.getSpecName())){
                criteria.andSpecNameLike("%"+spec.getSpecName()+"%");
            }
        }
        Page<Specification> brandList = (Page<Specification>)specDao.selectByExample(specQuery);
        return new PageResult(brandList.getTotal(),brandList.getResult());
    }
}
