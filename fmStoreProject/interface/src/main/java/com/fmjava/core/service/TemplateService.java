package com.fmjava.core.service;

import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.template.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TemplateService {

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param searchTemp
     * @return
     */
    PageResult search(Integer page, Integer pageSize, TypeTemplate searchTemp);

    List<Map> selectOptionList();

    void add(TypeTemplate typeTemplate);

    TypeTemplate findOne(Long id);

    void update(TypeTemplate typeTemplate);

    void delete(Long[] idx);

    List<Map> findBySpecList(Long id);
}
