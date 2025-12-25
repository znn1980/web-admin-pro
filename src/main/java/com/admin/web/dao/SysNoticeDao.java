package com.admin.web.dao;

import com.admin.web.model.SysNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author znn
 */
public interface SysNoticeDao extends JpaRepository<SysNotice, Long>, JpaSpecificationExecutor<SysNotice> {
    /**
     * 查询通知公告表
     *
     * @param page 分页
     * @return 通知公告
     */
    Page<SysNotice> findByOrderByCreateTimestampDesc(Pageable page);

    /**
     * 查询通知公告表
     *
     * @param createUsername 发布用户
     * @param page           分页
     * @return 通知公告
     */
    Page<SysNotice> findByCreateUsernameOrderByCreateTimestampDesc(String createUsername, Pageable page);

    /**
     * 查询未读通知公告
     *
     * @param userId 用户ID
     * @return 数量
     */
    @Query(value = """
            SELECT COUNT(*)
            FROM sys_notice
            WHERE sys_notice.id NOT IN (
            	SELECT id
            	FROM sys_notice
            		JOIN sys_notices_users ON sys_notice.id = sys_notices_users.notice_id
            	WHERE sys_notices_users.user_id = :userId
            )
            """, nativeQuery = true)
    Long countUnreadByUserId(Long userId);
}
