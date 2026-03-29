package com.admin.web.service;

import com.admin.web.dao.SysUserChatDao;
import com.admin.web.model.SysUserChat;
import com.admin.web.model.vo.PageVo;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author znn
 */
@Service
public class SysUserChatService {
    private final SysUserChatDao sysUserChatDao;
    private final ChatMemoryRepository chatMemoryRepository;

    public SysUserChatService(SysUserChatDao sysUserChatDao, ChatMemoryRepository chatMemoryRepository) {
        this.sysUserChatDao = sysUserChatDao;
        this.chatMemoryRepository = chatMemoryRepository;
    }

    public Slice<SysUserChat> all(String username, PageVo vo) {
        return this.sysUserChatDao.findAll((root, query, builder) ->
                query.where(builder.equal(root.get("username"), username))
                        .orderBy(builder.desc(root.get("timestamp")))
                        .getRestriction(), PageVo.of(vo));
    }

    public List<Message> findByConversationId(String conversationId) {
        return this.chatMemoryRepository.findByConversationId(conversationId);
    }

    public void save(String username, String conversationId, String content) {
        if (!this.sysUserChatDao.existsByUsernameAndConversationId(username, conversationId)) {
            this.sysUserChatDao.save(new SysUserChat(username, conversationId, content));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByConversationId(String conversationId) {
        this.sysUserChatDao.deleteByConversationId(conversationId);
        this.chatMemoryRepository.deleteByConversationId(conversationId);
    }
}
