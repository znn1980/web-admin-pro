package com.admin.web.dao;

import com.admin.web.model.SysUserLog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author znn
 */
public interface SysUserLogDao extends JpaRepository<SysUserLog, Long> {
    /**
     * 通过用户名查询日志
     *
     * @param username 用户名
     * @param page     分页
     * @return 日志
     */
    Page<SysUserLog> findByUsernameOrderByTimestampDesc(String username, Pageable page);
}
