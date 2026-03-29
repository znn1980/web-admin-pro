package com.admin.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author znn
 */
@Entity
@Table(name = "SYS_USER_CHAT")
public class SysUserChat implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "CONVERSATION_ID")
    private String conversationId;
    @Column(name = "CONTENT", columnDefinition = "TEXT")
    private String content;
    @Column(name = "CREATE_TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public SysUserChat() {
        this.setId(null);
        this.setTimestamp(LocalDateTime.now());
    }

    public SysUserChat(String username, String conversationId, String content) {
        this();
        this.setUsername(username);
        this.setConversationId(conversationId);
        this.setContent(content);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SysUserChat{" +
                "id=" + this.getId() +
                ", username='" + this.getUsername() + '\'' +
                ", conversationId='" + this.getConversationId() + '\'' +
                ", content='" + this.getContent() + '\'' +
                ", timestamp='" + this.getTimestamp() + '\'' +
                '}';
    }
}
