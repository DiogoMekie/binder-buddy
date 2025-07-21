package com.diogomekie.binderbuddy.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WelcomeController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello from BinderBuddy Backend!";
    }

}
