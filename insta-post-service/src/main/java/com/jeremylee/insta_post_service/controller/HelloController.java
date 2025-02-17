package com.jeremylee.insta_post_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/greetings")
    public String greetingMessage() {
        return "Hello greetings!";
    }

    @GetMapping("/welcome")
    public String welcomeMessage() {
        return "Hello welcome!";
    }

}
