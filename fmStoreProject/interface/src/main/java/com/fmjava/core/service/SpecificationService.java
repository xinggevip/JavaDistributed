package com.fmjava.core.service;

import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.specification.Specification;

public interface SpecificationService {

    /**
     * 分页查询规格
     * @param spec
     * @param page
     * @param rows
     * @return
     */
    PageResult findPage(Specification spec, Integer page, Integer rows);
}
