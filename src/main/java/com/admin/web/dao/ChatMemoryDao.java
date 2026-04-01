package com.admin.web.dao;

import com.admin.web.model.ChatMemory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author znn
 */
@Repository
public class ChatMemoryDao {
    private final JdbcTemplate jdbcTemplate;

    public ChatMemoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ChatMemory> findByConversationId(String conversationId) {
        return this.jdbcTemplate.query("""
                SELECT spring_ai_chat_memory.*
                FROM spring_ai_chat_memory
                WHERE spring_ai_chat_memory.conversation_id = ?
                ORDER BY spring_ai_chat_memory.timestamp
                """, (rs, rowNum) ->
                new ChatMemory(
                        rs.getString("conversation_id"),
                        rs.getString("content"),
                        rs.getString("type"),
                        rs.getObject("timestamp", Timestamp.class).getTime()
                ), conversationId);
    }

    public void deleteByConversationId(String conversationId) {
        this.jdbcTemplate.update("""
                DELETE FROM spring_ai_chat_memory WHERE spring_ai_chat_memory.conversation_id = ?
                """, conversationId);
    }
}
