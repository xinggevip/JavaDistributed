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
import com.fmjava.utils.Constants;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult findPage(TypeTemplate template, Integer page, Integer pageSize) {

        if (!redisTemplate.hasKey(Constants.BRAND_LIST_REDIS) || !redisTemplate.hasKey(Constants.SPEC_LIST_REDIS)){
            System.out.println("redis缓存品牌与规格");
            //查询出所有的模板
            List<TypeTemplate> typeTemplates = templateDao.selectByExample(null);
            for (TypeTemplate typeTemplate : typeTemplates) {
                //1.取出品牌
                String brandIds = typeTemplate.getBrandIds();
                //转成json
                List<Map> brandList = JSON.parseArray(brandIds, Map.class);
                redisTemplate.boundHashOps(Constants.BRAND_LIST_REDIS)
                        .put(typeTemplate.getId(),brandList);
                //2.取出规格
                List<Map> bySpecList = this.findBySpecList(typeTemplate.getId());
                redisTemplate.boundHashOps(Constants.SPEC_LIST_REDIS)
                        .put(typeTemplate.getId(),bySpecList);

            }

        }

       PageHelper.startPage(page, pageSize);
        TypeTemplateQuery query = new TypeTemplateQuery();
        TypeTemplateQuery.Criteria criteria = query.createCriteria();
        if (template != null) {
            if (template.getName() != null && !"".equals(template.getName())) {
                criteria.andNameLike("%"+template.getName()+"%");
            }
        }
        Page<TypeTemplate> templateList =  (Page<TypeTemplate>)templateDao.selectByExample(query);
        return new PageResult(templateList.getResult(),templateList.getTotal());
    }

    @Override
    public void add(TypeTemplate template) {
        templateDao.insertSelective(template);
    }

    @Override
    public TypeTemplate findOne(Long id) {

        return templateDao.selectByPrimaryKey(id);
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

    @Override
    public PageResult search(Integer page, Integer pageSize, TypeTemplate searchTemp) {
        PageHelper.startPage(page, pageSize);
        TypeTemplateQuery query = new TypeTemplateQuery();
        TypeTemplateQuery.Criteria criteria = query.createCriteria();
        if (searchTemp != null && !"".equals(searchTemp)){
            if (searchTemp.getName() != null && !"".equals(searchTemp.getName())){
                criteria.andNameLike(searchTemp.getName());
            }
        }
        Page<TypeTemplate> typeTemplates = (Page<TypeTemplate>)templateDao.selectByExample(query);
        return new PageResult(typeTemplates.getResult(),typeTemplates.getTotal());
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
    public List<Map> selectOptionList() {
        return templateDao.selectOptionList();
    }
}
