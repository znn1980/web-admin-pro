package com.admin.web.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * @author znn
 */
public class UserLogVo extends PageVo {
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTimestamp;

    public UserLogVo() {
        this.setUsername(null);
        this.setStartTimestamp(null);
        this.setEndTimestamp(null);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(LocalDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public LocalDateTime getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(LocalDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() +
                "UserLogVo{" +
                "username='" + this.getUsername() + '\'' +
                ", startTimestamp=" + this.getStartTimestamp() +
                ", endTimestamp=" + this.getEndTimestamp() +
                '}';
    }
}
