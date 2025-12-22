package com.admin.web.dao;

import com.admin.web.model.SysNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author znn
 */
public interface SysNoticeDao extends JpaRepository<SysNotice, Long>, JpaSpecificationExecutor<SysNotice> {
}
