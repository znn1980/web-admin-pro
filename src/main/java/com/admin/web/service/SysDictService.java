package com.admin.web.service;

import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.entity.SysDict;
import com.admin.web.model.enums.ResponseCode;
import com.admin.web.model.request.PageRequest;
import com.admin.web.model.request.SearchRequest;
import com.admin.web.repository.SysDictRepository;
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
public class SysDictService {
    private final SysDictRepository sysDictRepository;

    public SysDictService(SysDictRepository sysDictRepository) {
        this.sysDictRepository = sysDictRepository;
    }

    public Page<SysDict> all(SearchRequest request) {
        return this.sysDictRepository.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.search())) {
                String search = String.format("%%%s%%", request.search().toLowerCase());
                predicates.add(builder.or(
                        builder.like(builder.lower(root.get("name")), search)
                        , builder.like(builder.lower(root.get("key")), search)
                ));
            }
            return Objects.requireNonNull(query)
                    .where(predicates.toArray(new Predicate[0]))
                    .getRestriction();
        }, PageRequest.of(request.page(), request.limit(), request.sort()));
    }


    public void create(SysDict sysDict) {
        if (Objects.nonNull(this.sysDictRepository.findByName(sysDict.getName()))) {
            throw new ServerResponseException("字典名称已存在！");
        }
        if (Objects.nonNull(this.sysDictRepository.findByKey(sysDict.getKey()))) {
            throw new ServerResponseException("字典键名已存在！");
        }
        this.sysDictRepository.save(sysDict);
    }

    public void update(SysDict sysDict) {
        SysDict oldSysDict = this.sysDictRepository.findById(sysDict.getId())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (!Objects.equals(oldSysDict.getName(), sysDict.getName())
                && Objects.nonNull(this.sysDictRepository.findByName(sysDict.getName()))) {
            throw new ServerResponseException("字典名称已存在！");
        }
        if (!Objects.equals(oldSysDict.getKey(), sysDict.getKey())
                && Objects.nonNull(this.sysDictRepository.findByKey(sysDict.getKey()))) {
            throw new ServerResponseException("字典键名已存在！");
        }
        BeanUtils.copyNonNullProperties(sysDict, oldSysDict);
        this.sysDictRepository.save(oldSysDict);
    }

    public void delete(List<Long> id) {
        this.sysDictRepository.deleteAllById(id);
    }
}
