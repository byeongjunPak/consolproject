package com.console.mall.entitiy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;
    private String roomId;
    private String content;
    private String senderId;
    private MessageType type;

    public enum MessageType {
        JOIN, CHAT, EXIT
    }

}
