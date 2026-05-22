package com.admin.web.service;

import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.entity.SysConfig;
import com.admin.web.model.enums.ResponseCode;
import com.admin.web.model.request.PageRequest;
import com.admin.web.model.request.SearchRequest;
import com.admin.web.repository.SysConfigRepository;
import com.admin.web.utils.BeanUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author znn
 */
@Service
public class SysConfigService {
    private final SysConfigRepository sysConfigRepository;

    public SysConfigService(SysConfigRepository sysConfigRepository) {
        this.sysConfigRepository = sysConfigRepository;
    }

    public Page<SysConfig> all(SearchRequest request) {
        return this.sysConfigRepository.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.search())) {
                String search = String.format("%%%s%%", request.search().toLowerCase());
                predicates.add(builder.or(
                        builder.like(builder.lower(root.get("key")), search)
                        , builder.like(builder.lower(root.get("name")), search)
                ));
            }
            return Objects.requireNonNull(query)
                    .where(predicates.toArray(new Predicate[0]))
                    .getRestriction();
        }, PageRequest.of(request.page(), request.limit(), request.sort()));
    }

    public void create(SysConfig sysConfig) {
        if (Objects.nonNull(this.sysConfigRepository.findByName(sysConfig.getName()))) {
            throw new ServerResponseException("参数名称已存在！");
        }
        if (Objects.nonNull(this.sysConfigRepository.findByKey(sysConfig.getKey()))) {
            throw new ServerResponseException("参数键名已存在！");
        }
        this.sysConfigRepository.save(sysConfig);
    }

    public void update(SysConfig sysConfig) {
        SysConfig oldSysConfig = this.sysConfigRepository.findById(sysConfig.getId())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (!Objects.equals(oldSysConfig.getName(), sysConfig.getName())
                && Objects.nonNull(this.sysConfigRepository.findByName(sysConfig.getName()))) {
            throw new ServerResponseException("参数名称已存在！");
        }
        if (!Objects.equals(oldSysConfig.getKey(), sysConfig.getKey())
                && Objects.nonNull(this.sysConfigRepository.findByKey(sysConfig.getKey()))) {
            throw new ServerResponseException("参数键名已存在！");
        }
        BeanUtils.copyNonNullProperties(sysConfig, oldSysConfig);
        this.sysConfigRepository.save(oldSysConfig);
    }

    public void delete(List<Long> id) {
        this.sysConfigRepository.deleteAllById(id);
    }
}
