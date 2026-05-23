package com.admin.web.service;

import com.admin.web.model.entity.SysDict;
import com.admin.web.repository.SysDictRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author znn
 */
@Service
public class SysDictionariesService {
    private final SysDictRepository sysDictRepository;

    public SysDictionariesService(SysDictRepository sysDictRepository) {
        this.sysDictRepository = sysDictRepository;
    }

    /**
     * Thymeleaf模板中获取指定字典列表
     *
     * <pre>
     * <select>
     *    <option th:each="dict : ${#dicts.asKey('sys_sex')}" th:text="${dict.label()}" th:value="${dict.value()}"></option>
     * </select>
     * </pre>
     *
     * @param key 键名
     * @return 字典列表
     */
    public List<Dict> asKey(String key) {
        SysDict sysDict = this.sysDictRepository.findByKey(key);
        return Objects.isNull(sysDict) || sysDict.isDisable() ? List.of() : sysDict.getDetails().stream()
                .filter(detail -> !detail.isDisable())
                .map(detail -> new Dict(detail.getLabel(), detail.getValue())).toList();
    }

    public record Dict(String label, String value) {
    }
}
