//package com.example.chatapp.controller;
//
//import com.example.chatapp.service.ChatGroupService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/groups")
//public class ChatGroupController {
//
//    private final ChatGroupService chatGroupService;
//
//    @PostMapping("/create")
//    public ChatGroup createGroup(@RequestParam String groupName, @RequestParam String createdBy) {
//        return chatGroupService.createGroup(groupName, createdBy);
//    }
//
//    @PostMapping("/{groupId}/addUser")
//    public void addUserToGroup(@PathVariable Long groupId, @RequestParam Long userId) {
//        chatGroupService.addUserToGroup(groupId, userId);
//    }
//
//    @GetMapping("/user/{userId}")
//    public List<ChatGroup> getUserGroups(@PathVariable Long userId) {
//        return chatGroupService.getUserGroups(userId);
//    }
//}
