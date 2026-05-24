package com.admin.web.repository;

import com.admin.web.model.entity.SysNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author znn
 */
public interface SysNoticeRepository extends JpaRepository<SysNotice, Long>, JpaSpecificationExecutor<SysNotice> {
}
