package com.fmjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.core.pojo.template.TypeTemplate;
import com.fmjava.core.service.TemplateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/temp")
public class TemplateController {
    @Reference
    private TemplateService templateService;

    @RequestMapping("/findOne")
    public TypeTemplate findOne(Long id) {
        return templateService.findOne(id);
    }

    /**
     * 查询规格

     [{"id":43,"spec_name":"选择版本","option":['6G','8G']}]

     */
    @RequestMapping("/findBySpecList")
    public List<Map>findBySpecList(Long id){
        List<Map> maps = this.templateService.findBySpecList(id);
        return maps;
    }


}