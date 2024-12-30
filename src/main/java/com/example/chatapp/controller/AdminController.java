//package com.example.chatapp.controller;
//
//import com.example.chatapp.service.AdminService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class AdminController {
//
//    private final AdminService adminService;
//
//    @DeleteMapping("/admin/messages/{id}")
//    public void deleteMessage(@PathVariable Long id) {
//        adminService.deleteMessage(id);
//    }
//
//    @DeleteMapping("/admin/users/{id}")
//    public void deleteUser(@PathVariable Long id) {
//        adminService.deleteUser(id);
//    }
//
//    @GetMapping("/admin/users/{id}/ban")
//    public void banUser(@PathVariable Long id) {
//        adminService.banUser(id);
//    }
//}