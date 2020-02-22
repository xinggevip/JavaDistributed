package com.fmjava.core.service;

import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.entity.SpecEntity;
import com.fmjava.core.pojo.specification.Specification;

import java.util.List;
import java.util.Map;

public interface SpecificationService {

    /**
     * 分页查询规格
     */
    PageResult findPage(Integer page, Integer pageSize, Specification brand);

    void add(SpecEntity specEntity);

    SpecEntity findOne(Long id);

    void update(SpecEntity specEntity);

    void delete(Long[] ids);

    List<Map> selectOptionList();
}
