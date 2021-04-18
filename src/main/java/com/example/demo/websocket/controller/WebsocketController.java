package com.example.demo.websocket.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/socket")
public class WebsocketController {

    @RequestMapping(value = "")
    public ModelAndView list(){
        return new ModelAndView("websocket/websocket");
    }
}
