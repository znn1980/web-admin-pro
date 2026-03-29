package com.admin.web.model;

import java.time.LocalDateTime;

/**
 * @author znn
 */
public record ChatMemory(String conversationId, String content, String type, LocalDateTime timestamp) {
}
