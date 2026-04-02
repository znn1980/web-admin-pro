package com.admin.web.service;

import com.admin.web.dao.ChatMemoryDao;
import com.admin.web.dao.SysUserChatDao;
import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.ChatMemory;
import com.admin.web.model.SysUser;
import com.admin.web.model.SysUserChat;
import com.admin.web.model.enums.ResponseCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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
    private final ChatMemoryDao chatMemoryDao;

    public SysUserChatService(SysUserChatDao sysUserChatDao, ChatMemoryDao chatMemoryDao) {
        this.sysUserChatDao = sysUserChatDao;
        this.chatMemoryDao = chatMemoryDao;
    }

    public Slice<SysUserChat> findByUsername(SysUser sysUser, PageRequest pageRequest) {
        return this.sysUserChatDao.findAll((root, query, builder) ->
                query.where(builder.equal(root.get("username"), sysUser.getUsername()))
                        .orderBy(builder.desc(root.get("timestamp")))
                        .getRestriction(), pageRequest);
    }

    public SysUserChat findByUsernameAndConversationId(SysUser sysUser, String conversationId) {
        return this.sysUserChatDao.findByUsernameAndConversationId(sysUser.getUsername(), conversationId);
    }

    public List<ChatMemory> findByConversationId(String conversationId) {
        return this.chatMemoryDao.findByConversationId(conversationId);
    }

    public void save(SysUser sysUser, String conversationId, String content) {
        SysUserChat sysUserChat = Optional.ofNullable(this.sysUserChatDao.findByUsernameAndConversationId(sysUser.getUsername(), conversationId))
                .orElseGet(() -> new SysUserChat(sysUser.getUsername(), conversationId, content));
        sysUserChat.setTimestamp(LocalDateTime.now());
        this.sysUserChatDao.save(sysUserChat);
    }

    public void update(SysUser sysUser, String conversationId, String content) {
        SysUserChat sysUserChat = Optional.ofNullable(this.sysUserChatDao.findByUsernameAndConversationId(sysUser.getUsername(), conversationId))
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        sysUserChat.setContent(content);
        sysUserChat.setTimestamp(LocalDateTime.now());
        this.sysUserChatDao.save(sysUserChat);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(SysUser sysUser, String conversationId) {
        if ("all".equalsIgnoreCase(conversationId)) {
            this.sysUserChatDao.findByUsername(sysUser.getUsername()).forEach(sysUserChat ->
                    this.sysUserChatDao.deleteByConversationId(sysUserChat.getConversationId()));
        } else {
            Optional.ofNullable(this.sysUserChatDao.findByUsernameAndConversationId(sysUser.getUsername(), conversationId))
                    .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
            this.sysUserChatDao.deleteByConversationId(conversationId);
            this.chatMemoryDao.deleteByConversationId(conversationId);
        }
    }

}
