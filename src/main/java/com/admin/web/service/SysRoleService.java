package com.admin.web.service;

import com.admin.web.model.request.WhereRequest;
import com.admin.web.model.SysMove;
import com.admin.web.repository.SysRoleRepository;
import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.entity.SysRole;
import com.admin.web.model.enums.ResponseCode;
import com.admin.web.model.request.MoveRequest;
import com.admin.web.utils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author znn
 */
@Service
public class SysRoleService {
    private final SysRoleRepository sysRoleRepository;

    public SysRoleService(SysRoleRepository sysRoleRepository) {
        this.sysRoleRepository = sysRoleRepository;
    }

    public List<SysRole> all(String search) {
        return this.sysRoleRepository.findAll((root, query, builder) ->
                Objects.requireNonNull(query).where(WhereRequest.builder()
                        .add(StringUtils.hasText(search), () ->
                                builder.like(builder.lower(root.get("name")), String.format("%%%s%%", search.toLowerCase())))
                        .build()
                ).orderBy(builder.asc(root.get("sort"))).getRestriction());
    }

    @Transactional(rollbackFor = Exception.class)
    public void move(MoveRequest request) {
        SysRole sysRole = this.sysRoleRepository.findById(request.id())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        List<SysRole> sysRoles = this.sysRoleRepository.findByOrderBySort();
        SysMove.of(request.move(), sysRole.getId(), sysRoles).move((index) -> {
            long sort = sysRole.getSort();
            sysRole.setSort(sysRoles.get(index).getSort());
            sysRoles.get(index).setSort(sort);
            this.sysRoleRepository.saveAll(Arrays.asList(sysRole, sysRoles.get(index)));
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(SysRole sysRole) {
        if (Objects.nonNull(this.sysRoleRepository.findByName(sysRole.getName()))) {
            throw new ServerResponseException("角色名称已存在！");
        }
        this.sysRoleRepository.save(sysRole);
        sysRole.setSort(sysRole.getId());
        this.sysRoleRepository.save(sysRole);
    }

    public void update(SysRole sysRole) {
        SysRole oldSysRole = this.sysRoleRepository.findById(sysRole.getId())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (!Objects.equals(oldSysRole.getName(), sysRole.getName())
                && Objects.nonNull(this.sysRoleRepository.findByName(sysRole.getName()))) {
            throw new ServerResponseException("角色名称已存在！");
        }
        BeanUtils.copyNonNullProperties(sysRole, oldSysRole);
        this.sysRoleRepository.save(oldSysRole);
    }

    public void delete(Long id) {
        SysRole sysRole = this.sysRoleRepository.findById(id)
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (this.sysRoleRepository.existsByRoleId(sysRole.getId())) {
            throw new ServerResponseException("此角色已绑定用户，请先解绑用户下的角色！");
        }
        this.sysRoleRepository.deleteById(id);
    }
}
