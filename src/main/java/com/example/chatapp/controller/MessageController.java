package com.example.chatapp.controller;

import com.example.chatapp.model.Message;
import com.example.chatapp.service.ChatbotService;
import com.example.chatapp.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final ChatbotService chatbotService;

    @GetMapping("/api/messages")
    public List<Message> getAllMessages() {
        return messageService.findAllMessages();
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message chatMessage) {
        // Lưu tin nhắn của người dùng
        Message userMessage = Message.builder()
                .sender(chatMessage.getSender())
                .content(chatMessage.getContent())
                .timestamp(LocalDateTime.now())
                .type(chatMessage.getType())
                .build();
        messageService.saveMessage(userMessage);

        String query;
        String botType = null;

        if (chatMessage.getContent().startsWith("@Chatbot1")) {
            query = chatMessage.getContent().substring(9).trim();
            botType = "blenderbot";
        } else if (chatMessage.getContent().startsWith("@Chatbot2")) {
            query = chatMessage.getContent().substring(9).trim();
            botType = "qa-bot";
        } else {
            return userMessage; // Nếu không gọi bot, trả về tin nhắn bình thường
        }

        String chatbotResponse = chatbotService.getResponse(query, botType);

        Message botMessage = Message.builder()
                .sender(botType.equals("blenderbot") ? "Chatbot1" : "Chatbot2")
                .content(chatbotResponse)
                .timestamp(LocalDateTime.now())
                .type(Message.MessageType.CHAT)
                .build();

        messageService.saveMessage(botMessage);

        return botMessage;
    }
}
