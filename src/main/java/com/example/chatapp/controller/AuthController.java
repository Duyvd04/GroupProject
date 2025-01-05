package com.example.chatapp.controller;

import com.example.chatapp.security.JwtTokenProvider;
import com.example.chatapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        try {
            if (userService.validateUser(username, password)) {
                // Generate JWT token
                String token = jwtTokenProvider.createToken(username);

                // Set token in HTTP-only cookie
                ResponseCookie cookie = ResponseCookie.from("jwt", token)
                        .httpOnly(true)
                        .secure(false) // Set to true if using HTTPS
                        .path("/")
                        .maxAge(3600) // 1 hour expiration
                        .build();

                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body("Login successful");
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during login");
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
