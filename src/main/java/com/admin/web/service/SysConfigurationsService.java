package com.admin.web.service;

import com.admin.web.model.entity.SysConfig;
import com.admin.web.repository.SysConfigRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author znn
 */
@Service
public class SysConfigurationsService {
    private final SysConfigRepository sysConfigRepository;

    public SysConfigurationsService(SysConfigRepository sysConfigRepository) {
        this.sysConfigRepository = sysConfigRepository;
    }

    /**
     * Thymeleaf模板获取指定参数配置
     *
     * <pre>
     *     <span th:text="${#configs.asKey('sys_skin','blue')}"></span>
     * </pre>
     *
     * @param key          键名
     * @param defaultValue 默认值
     * @return 参数配置
     */
    public String asKey(String key, String defaultValue) {
        SysConfig sysConfig = this.sysConfigRepository.findByKey(key);
        return Objects.isNull(sysConfig) || sysConfig.isDisable() ? defaultValue : sysConfig.getValue();
    }

    public String asKey(String key) {
        return this.asKey(key, "");
    }
}
