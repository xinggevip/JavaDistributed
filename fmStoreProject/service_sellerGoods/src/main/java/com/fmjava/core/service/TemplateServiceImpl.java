package com.fmjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.fmjava.core.dao.specification.SpecificationOptionDao;
import com.fmjava.core.dao.template.TypeTemplateDao;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.specification.SpecificationOption;
import com.fmjava.core.pojo.specification.SpecificationOptionQuery;
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
    @Autowired
    private SpecificationOptionDao specificationOptionDao;

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

    @Override
    public List<Map> findBySpecList(Long id) {
        //1.根据id查询模板
        TypeTemplate template = templateDao.selectByPrimaryKey(id);
        //2.从模板当中获取规格的集合
        //[{"id":42,"spec_name":"选择颜色"},{"id":43,"spec_name":"选择版本"},{"id":44,"spec_name":"套　　餐"}]
        String specIds = template.getSpecIds();
        List<Map> mapList = JSON.parseArray(specIds,Map.class);
        if (mapList !=null){
            for (Map map : mapList) {
                //{"id":42,"spec_name":"选择颜色"}
                Long specId = Long.parseLong(String.valueOf(map.get("id")));
                //根据id查询规格选项
                SpecificationOptionQuery optionQuery = new SpecificationOptionQuery();
                SpecificationOptionQuery.Criteria criteria = optionQuery.createCriteria();
                criteria.andSpecIdEqualTo(specId);
                List<SpecificationOption> specificationOptions = specificationOptionDao.selectByExample(optionQuery);
                map.put("option",specificationOptions);//{"id":42,"spec_name":"选择颜色","option":[{},{},{}]}
            }
            return mapList;
        }
        return null;
    }


}
