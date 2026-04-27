package com.admin.web.service;

import com.admin.web.repository.SysRoleRepository;
import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.SysRole;
import com.admin.web.model.enums.Move;
import com.admin.web.model.enums.ResponseCode;
import com.admin.web.model.vo.MoveVo;
import com.admin.web.utils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author znn
 */
@Service
public class SysRoleService {
    private final SysRoleRepository sysRoleRepository;

    public SysRoleService(SysRoleRepository sysRoleRepository) {
        this.sysRoleRepository = sysRoleRepository;
    }

    public List<SysRole> all() {
        return this.sysRoleRepository.findByOrderBySort();
    }

    @Transactional(rollbackFor = Exception.class)
    public void move(MoveVo vo) {
        SysRole sysRole = this.sysRoleRepository.findById(vo.id())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        List<SysRole> sysRoles = this.sysRoleRepository.findByOrderBySort();
        boolean isUp = Objects.equals(Move.UP, vo.move());
        int index = IntStream.range(0, sysRoles.size())
                .filter(i -> Objects.equals(sysRole.getId(), sysRoles.get(i).getId()))
                .findFirst().orElse(-1);
        if (index == -1 || (isUp ? index <= 0 : index >= sysRoles.size() - 1)) {
            return;
        }
        SysRole oldSysRole = sysRoles.get(isUp ? index - 1 : index + 1);
        Long oldSysRoleSort = oldSysRole.getSort();
        oldSysRole.setSort(sysRole.getSort());
        sysRole.setSort(oldSysRoleSort);
        this.sysRoleRepository.saveAll(Arrays.asList(sysRole, oldSysRole));
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
