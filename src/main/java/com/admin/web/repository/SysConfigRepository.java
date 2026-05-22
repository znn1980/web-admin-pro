package com.admin.web.repository;

import com.admin.web.model.entity.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author znn
 */
public interface SysConfigRepository extends JpaRepository<SysConfig, Long>, JpaSpecificationExecutor<SysConfig> {
    /**
     * 根据名称查询参数配置
     *
     * @param name 名称
     * @return 参数配置
     */
    SysConfig findByName(String name);

    /**
     * 根据键名查询参数配置
     *
     * @param key 键名
     * @return 参数配置
     */
    SysConfig findByKey(String key);
}
