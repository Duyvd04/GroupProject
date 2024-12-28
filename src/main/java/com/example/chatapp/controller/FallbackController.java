package com.example.chatapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FallbackController {

    @RequestMapping("/**")
    public String redirectToLogin() {
        return "redirect:/login"; // Redirect any unmatched URL to login
    }
}
