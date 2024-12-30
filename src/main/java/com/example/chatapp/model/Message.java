package com.example.chatapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;  // Store the username of the sender
    private String receiver; // Store the username of the receiver
    @Column(columnDefinition = "TEXT") // Use TEXT to support long messages
    private String content;
    private LocalDateTime timestamp;
    @Enumerated(EnumType.STRING)
    private MessageType type;

    public enum MessageType {
        CHAT, LEAVE, JOIN
    }
}
