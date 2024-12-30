package com.example.chatapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    @GetMapping("/settings")
    public String settingsPage(@RequestParam(required = false, defaultValue = "en") String lang, Model model) {
        model.addAttribute("currentLanguage", lang);
        return "settings";
    }
}