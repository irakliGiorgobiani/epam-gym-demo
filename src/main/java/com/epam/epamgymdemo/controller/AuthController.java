package com.epam.epamgymdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/v1")
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/login-error")
    public String loginError() {
        return "login-error.html";
    }
}
