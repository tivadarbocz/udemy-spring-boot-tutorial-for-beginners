package com.in28minutes.controller;

import com.in28minutes.configuration.BasicConfiguration;
import com.in28minutes.service.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 2017.01.24..
 */
@RestController
public class WelcomeController {

    @Autowired
    private WelcomeService welcomeService;

    @Autowired
    private BasicConfiguration basicConfiguration;

    @RequestMapping("/welcome")
    public String welcome(){
        return welcomeService.retrieveWelcomeMessage();
    }

    @RequestMapping("/dynamic-configuration")
    public Map dynamicConfiguration(){
        Map map = new HashMap<>();
        map.put("message", basicConfiguration.getMessage());
        map.put("number", basicConfiguration.getNumber());
        map.put("value", basicConfiguration.isValue());

        return map;
    }
}
