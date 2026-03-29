package com.admin.web.service;

import com.admin.web.dao.SysUserChatDao;
import com.admin.web.model.ChatMemory;
import com.admin.web.model.SysUserChat;
import com.admin.web.model.vo.PageVo;
import org.springframework.data.domain.Slice;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author znn
 */
@Service
public class SysUserChatService {
    private final SysUserChatDao sysUserChatDao;
    private final JdbcTemplate jdbcTemplate;

    public SysUserChatService(SysUserChatDao sysUserChatDao, JdbcTemplate jdbcTemplate) {
        this.sysUserChatDao = sysUserChatDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Slice<SysUserChat> all(String username, PageVo vo) {
        return this.sysUserChatDao.findAll((root, query, builder) ->
                query.where(builder.equal(root.get("username"), username))
                        .orderBy(builder.desc(root.get("timestamp")))
                        .getRestriction(), PageVo.of(vo));
    }

    public void save(String username, String conversationId, String content) {
        SysUserChat sysUserChat = Optional.ofNullable(this.sysUserChatDao.findByUsernameAndConversationId(username, conversationId))
                .orElseGet(() -> new SysUserChat(username, conversationId, content));
        sysUserChat.setTimestamp(LocalDateTime.now());
        this.sysUserChatDao.save(sysUserChat);
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
                        rs.getObject("timestamp", LocalDateTime.class)
                ), conversationId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByConversationId(String conversationId) {
        this.sysUserChatDao.deleteByConversationId(conversationId);
        this.jdbcTemplate.update("""
                DELETE FROM spring_ai_chat_memory WHERE spring_ai_chat_memory.conversation_id = ?
                """, conversationId);
    }
}
