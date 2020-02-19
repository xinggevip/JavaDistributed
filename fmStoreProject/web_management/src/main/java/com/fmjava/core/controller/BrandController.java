package com.fmjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.service.BrandService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    // 分页查询品牌
    @RequestMapping("/findPage")
    public PageResult getallbrands(Integer page,Integer rows){
        PageResult allBrands = brandService.findAllBrands(page, rows);
        return allBrands;
    }
}
