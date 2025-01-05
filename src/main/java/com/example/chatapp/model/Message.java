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

    private String sender;
    private String receiver;
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDateTime timestamp;
    @Enumerated(EnumType.STRING)
    private MessageType type;

    public enum MessageType {
        CHAT, LEAVE, JOIN
    }
}
