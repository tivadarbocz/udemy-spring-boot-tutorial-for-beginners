package com.in28minutes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by Admin on 2017.01.24..
 */
@Service
public class WelcomeService {

    @Value("${welcome.message}")
    private String welcomeMessage;

    public String retrieveWelcomeMessage(){
        return welcomeMessage;
    }
}
