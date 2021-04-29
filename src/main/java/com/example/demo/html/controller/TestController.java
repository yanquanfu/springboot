package com.example.demo.html.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(value = "")
    public ModelAndView list(){
        return new ModelAndView("/test/test");
    }
}
