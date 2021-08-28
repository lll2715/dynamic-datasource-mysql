package com.example.mysql.controller;

import com.example.mysql.model.UmsAdmin;
import com.example.mysql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 品牌管理示例controller
 */
@RestController
public class DemoController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/get/{id}")
    public UmsAdmin get(@PathVariable("id") Long id) {
        return userService.insert(id);



    }


}
