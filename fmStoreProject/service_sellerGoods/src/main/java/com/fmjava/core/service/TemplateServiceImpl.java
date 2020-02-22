package com.fmjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.template.TypeTemplateDao;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.template.TypeTemplate;
import com.fmjava.core.pojo.template.TypeTemplateQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TypeTemplateDao templateDao;

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param searchTemp
     * @return
     */
    @Override
    public PageResult search(Integer page, Integer pageSize, TypeTemplate searchTemp) {

        PageHelper.startPage(page,pageSize);

        // 降序排序
        TypeTemplateQuery typeTemplateQuery = new TypeTemplateQuery();
        typeTemplateQuery.setOrderByClause("id desc");

        // 匹配关键词
        if (searchTemp != null){
            TypeTemplateQuery.Criteria criteria = typeTemplateQuery.createCriteria();
            if (searchTemp.getName() != null && !"".equals(searchTemp.getName())){
                criteria.andNameLike("%" + searchTemp.getName() + "%");
            }
        }

        // 执行查询
        Page<TypeTemplate> typeTemplates = (Page<TypeTemplate>)templateDao.selectByExample(typeTemplateQuery);

        PageResult pageResult = new PageResult(typeTemplates.getTotal(), typeTemplates.getResult());

        return pageResult;
    }

    @Override
    public List<Map> selectOptionList() {
        return templateDao.selectOptionList();
    }

    @Override
    public void add(TypeTemplate typeTemplate) {
        templateDao.insertSelective(typeTemplate);
    }

    @Override
    public TypeTemplate findOne(Long id) {
        return templateDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(TypeTemplate typeTemplate) {
        templateDao.updateByPrimaryKeySelective(typeTemplate);
    }

    @Override
    public void delete(Long[] idx) {
        for (Long aLong : idx) {
            templateDao.deleteByPrimaryKey(aLong);
        }
    }


}
