package com.admin.web.service;

import com.admin.web.dao.SysRoleDao;
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
    private final SysRoleDao sysRoleDao;

    public SysRoleService(SysRoleDao sysRoleDao) {
        this.sysRoleDao = sysRoleDao;
    }

    public List<SysRole> all() {
        return this.sysRoleDao.findByOrderBySort();
    }

    @Transactional(rollbackFor = Exception.class)
    public void move(MoveVo vo) {
        SysRole sysRole = this.sysRoleDao.findById(vo.getId())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        List<SysRole> sysRoles = this.sysRoleDao.findByOrderBySort();
        boolean isUp = Objects.equals(Move.UP, vo.getMove());
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
        this.sysRoleDao.saveAll(Arrays.asList(sysRole, oldSysRole));
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(SysRole sysRole) {
        if (Objects.nonNull(this.sysRoleDao.findByName(sysRole.getName()))) {
            throw new ServerResponseException("角色名称已存在！");
        }
        this.sysRoleDao.save(sysRole);
        sysRole.setSort(sysRole.getId());
        this.sysRoleDao.save(sysRole);
    }

    public void update(SysRole sysRole) {
        SysRole oldSysRole = this.sysRoleDao.findById(sysRole.getId())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (!Objects.equals(oldSysRole.getName(), sysRole.getName())
                && Objects.nonNull(this.sysRoleDao.findByName(sysRole.getName()))) {
            throw new ServerResponseException("角色名称已存在！");
        }
        BeanUtils.copyNonNullProperties(sysRole, oldSysRole);
        this.sysRoleDao.save(oldSysRole);
    }

    public void delete(Long id) {
        SysRole sysRole = this.sysRoleDao.findById(id)
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (this.sysRoleDao.existsByRoleId(sysRole.getId())) {
            throw new ServerResponseException("此角色已绑定用户，请先解绑用户下的角色！");
        }
        this.sysRoleDao.deleteById(id);
    }
}
