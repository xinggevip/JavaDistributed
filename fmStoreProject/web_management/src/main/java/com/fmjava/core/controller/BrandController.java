package com.fmjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.entity.Result;
import com.fmjava.core.pojo.good.Brand;
import com.fmjava.core.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
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

    // 添加品牌
    @RequestMapping("/add")
    public Result addBrand(@RequestBody Brand brand){
        try {
            brandService.add(brand);
            Result result = new Result(true, "添加成功");
            return result;
        }catch (Exception e){
            System.out.println(e.getMessage());
            Result result = new Result(false, "添加失败");
            return result;
        }
    }

}
