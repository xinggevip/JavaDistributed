package com.fmjava.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.core.service.TestInterface;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Reference
    private TestInterface testInterface;

    @RequestMapping("/getname")
    public String getname(){
//        return "getname";
        return testInterface.getName(); // TestInterfaceImpl----dubbo
    }
}
