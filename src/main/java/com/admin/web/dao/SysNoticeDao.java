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
}
