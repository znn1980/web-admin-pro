package com.admin.web.dao;

import com.admin.web.model.SysUserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author znn
 */
public interface SysUserLogDao extends JpaRepository<SysUserLog, Long>, JpaSpecificationExecutor<SysUserLog> {
}
