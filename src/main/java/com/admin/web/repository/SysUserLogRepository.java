package com.admin.web.repository;

import com.admin.web.model.SysUserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author znn
 */
public interface SysUserLogRepository extends JpaRepository<SysUserLog, Long>, JpaSpecificationExecutor<SysUserLog> {
}
