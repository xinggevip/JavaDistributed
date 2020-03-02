package com.fmjava.core.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("/loginName")
    public String showName(){
        return   SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
