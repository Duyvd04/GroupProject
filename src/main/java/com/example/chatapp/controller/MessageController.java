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
        // Save the user's message
        Message userMessage = Message.builder()
                .sender(chatMessage.getSender())
                .content(chatMessage.getContent())
                .timestamp(LocalDateTime.now())
                .type(chatMessage.getType())
                .build();
        messageService.saveMessage(userMessage);

        // Check if the message is directed to the chatbot
        if (chatMessage.getContent().startsWith("@Chatbot")) {
            String query = chatMessage.getContent().substring(9).trim(); // Extract the user's query
            String chatbotResponse = chatbotService.getResponse(query); // Get the chatbot's response

            // Create a chatbot response message
            Message botMessage = Message.builder()
                    .sender("Chatbot")
                    .content(chatbotResponse)
                    .timestamp(LocalDateTime.now())
                    .type(Message.MessageType.CHAT)
                    .build();
            messageService.saveMessage(botMessage); // Save the chatbot's message

            return botMessage; // Return the chatbot's response to the chatroom
        }

        // Return the user's message for regular chat
        return userMessage;
    }
}
