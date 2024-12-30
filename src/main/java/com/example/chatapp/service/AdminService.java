//package com.example.chatapp.service;
//
//import com.example.chatapp.repository.MessageRepository;
//import com.example.chatapp.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AdminService {
//
//    private final MessageRepository messageRepository;
//    private final UserRepository userRepository;
//
//    public void deleteMessage(Long id) {
//        messageRepository.deleteById(id);
//    }
//
//    public void deleteUser(Long id) {
//        userRepository.deleteById(id);
//    }
//
//    public void banUser(Long id) {
//        userRepository.findById(id).ifPresent(user -> {
//            user.setEnabled(false); // Disable the user account
//            userRepository.save(user);
//        });
//    }
//}
