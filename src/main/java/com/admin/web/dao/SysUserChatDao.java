package com.admin.web.dao;

import com.admin.web.model.SysUserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author znn
 */
public interface SysUserChatDao extends JpaRepository<SysUserChat, Long>, JpaSpecificationExecutor<SysUserChat> {
    /**
     * 查询用户会话表
     *
     * @param username       用户名
     * @param conversationId 会话ID
     * @return 用户会话表
     */
    SysUserChat findByUsernameAndConversationId(String username, String conversationId);

    /**
     * 删除用户会话表
     *
     * @param conversationId 会话ID
     */
    void deleteByConversationId(String conversationId);
}
