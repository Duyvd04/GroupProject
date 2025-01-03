package com.example.chatapp.controller;

import com.example.chatapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login"; // Redirect root URL to login
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("error", false);
        return "register";
    }

    @PostMapping("/api/auth/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            userService.register(username, password);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", true);
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("error", error != null);
        return "login";
    }

    @GetMapping("/chat")
    public String chatPage(Model model) {
        model.addAttribute("logoutButton", true);
        return "chat";
    }
}