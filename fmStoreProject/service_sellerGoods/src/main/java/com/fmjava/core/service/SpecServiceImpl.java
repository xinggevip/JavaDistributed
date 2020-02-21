package com.fmjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.specification.SpecificationDao;
import com.fmjava.core.dao.specification.SpecificationOptionDao;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.entity.SpecEntity;
import com.fmjava.core.pojo.specification.Specification;
import com.fmjava.core.pojo.specification.SpecificationOption;
import com.fmjava.core.pojo.specification.SpecificationOptionQuery;
import com.fmjava.core.pojo.specification.SpecificationQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SpecServiceImpl implements SpecificationService  {
    @Autowired
    private SpecificationDao specDao;

    @Autowired
    private SpecificationOptionDao optionDao;

    @Override
    public PageResult findPage(Integer page, Integer pageSize, Specification spec) {
        PageHelper.startPage(page,pageSize);
        SpecificationQuery specQuery = new SpecificationQuery();
        specQuery.setOrderByClause("id desc");
        if (spec != null){
            SpecificationQuery.Criteria criteria = specQuery.createCriteria();
            if (spec.getSpecName() !=null && !"".equals(spec.getSpecName())){
                criteria.andSpecNameLike("%"+spec.getSpecName()+"%");
            }
        }
        Page<Specification> brandList = (Page<Specification>)specDao.selectByExample(specQuery);
        return new PageResult(brandList.getResult(),brandList.getTotal());
    }

    @Override
    public void add(SpecEntity specEntity) {
        //保存规格
        specDao.insertSelective(specEntity.getSpecification());
        //取出每一个规格  设置id 保存规格
        for (SpecificationOption specificationOption : specEntity.getSpecOptionList()) {
            //设置specId
            specificationOption.setSpecId(specEntity.getSpecification().getId());

            //保存规格选项
            optionDao.insertSelective(specificationOption);
        }
    }

    @Override
    public SpecEntity findOne(Long id) {

        Specification specification = specDao.selectByPrimaryKey(id);

        SpecificationOptionQuery query = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = query.createCriteria();
        criteria.andSpecIdEqualTo(id);
        List<SpecificationOption> optionList = optionDao.selectByExample(query);

        SpecEntity specEntity = new SpecEntity();
        specEntity.setSpecification(specification);
        specEntity.setSpecOptionList(optionList);

        return specEntity;
    }

    @Override
    public void update(SpecEntity specEntity) {
        // 1.先更新规格
        // 2.先打破关系，再重建关系
        // 3.根据id删除规格选项
        // 4.再遍历添加重建关系

        specDao.updateByPrimaryKeySelective(specEntity.getSpecification());

        // 打破关系
        SpecificationOptionQuery optionQuery = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = optionQuery.createCriteria();
        criteria.andSpecIdEqualTo(specEntity.getSpecification().getId());
        List<SpecificationOption> optionList = optionDao.selectByExample(optionQuery);
        for (SpecificationOption specificationOption : optionList) {
            optionDao.deleteByPrimaryKey(specificationOption.getId());
        }

        // 重建关系
        for (SpecificationOption specificationOption : specEntity.getSpecOptionList()) {
            specificationOption.setSpecId(specEntity.getSpecification().getId());
            optionDao.insertSelective(specificationOption);
        }


    }

    @Override
    public void delete(Long[] ids) {
        /**
         * 先删除规格选项，打破关系，再删除选项
         */

        SpecificationOptionQuery optionQuery = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = optionQuery.createCriteria();
        for (Long id : ids) {
            criteria.andSpecIdEqualTo(id);
        }
        optionDao.deleteByExample(optionQuery);

        for (Long id : ids) {
            specDao.deleteByPrimaryKey(id);
        }

    }
}
