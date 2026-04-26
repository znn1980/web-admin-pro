package com.admin.web.service;

import com.admin.web.repository.ChatMemoryRepository;
import com.admin.web.repository.SysUserChatRepository;
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
import java.util.Objects;
import java.util.Optional;

/**
 * @author znn
 */
@Service
public class SysUserChatService {
    private final SysUserChatRepository sysUserChatRepository;
    private final ChatMemoryRepository chatMemoryRepository;

    public SysUserChatService(SysUserChatRepository sysUserChatRepository, ChatMemoryRepository chatMemoryRepository) {
        this.sysUserChatRepository = sysUserChatRepository;
        this.chatMemoryRepository = chatMemoryRepository;
    }

    public Slice<SysUserChat> findByUsername(SysUser sysUser, PageRequest pageRequest) {
        return this.sysUserChatRepository.findAll((root, query, builder) ->
                Objects.requireNonNull(query)
                        .where(builder.equal(root.get("username"), sysUser.getUsername()))
                        .orderBy(builder.desc(root.get("timestamp")))
                        .getRestriction(), pageRequest);
    }

    public SysUserChat findByUsernameAndConversationId(SysUser sysUser, String conversationId) {
        return this.sysUserChatRepository.findByUsernameAndConversationId(sysUser.getUsername(), conversationId);
    }

    public List<ChatMemory> findByConversationId(String conversationId) {
        return this.chatMemoryRepository.findByConversationId(conversationId);
    }

    public void save(SysUser sysUser, String conversationId, String content) {
        SysUserChat sysUserChat = Optional.ofNullable(this.sysUserChatRepository.findByUsernameAndConversationId(sysUser.getUsername(), conversationId))
                .orElseGet(() -> new SysUserChat(sysUser.getUsername(), conversationId, content));
        sysUserChat.setTimestamp(LocalDateTime.now());
        this.sysUserChatRepository.save(sysUserChat);
    }

    public void update(SysUser sysUser, String conversationId, String content) {
        SysUserChat sysUserChat = Optional.ofNullable(this.sysUserChatRepository.findByUsernameAndConversationId(sysUser.getUsername(), conversationId))
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        sysUserChat.setContent(content);
        sysUserChat.setTimestamp(LocalDateTime.now());
        this.sysUserChatRepository.save(sysUserChat);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(SysUser sysUser, String conversationId) {
        if ("all".equalsIgnoreCase(conversationId)) {
            this.sysUserChatRepository.findByUsername(sysUser.getUsername()).forEach(sysUserChat ->
                    this.deleteByConversationId(sysUserChat.getConversationId()));
        } else {
            Optional.ofNullable(this.sysUserChatRepository.findByUsernameAndConversationId(sysUser.getUsername(), conversationId))
                    .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
            this.deleteByConversationId(conversationId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByConversationId(String conversationId) {
        this.sysUserChatRepository.deleteByConversationId(conversationId);
        this.chatMemoryRepository.deleteByConversationId(conversationId);
    }

}
