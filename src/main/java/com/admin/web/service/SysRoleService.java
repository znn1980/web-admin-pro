package com.admin.web.service;

import com.admin.web.dao.SysRoleDao;
import com.admin.web.exception.WebServerException;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysRole;
import com.admin.web.model.enums.Move;
import com.admin.web.model.vo.MoveVo;
import com.admin.web.utils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("角色不存在！")));
        List<SysRole> sysRoles = this.sysRoleDao.findByOrderBySort();
        int index = -1;
        for (int i = 0; i < sysRoles.size(); i++) {
            if (sysRoles.get(i).getId().equals(sysRole.getId())) {
                index = i;
                break;
            }
        }
        if (Move.UP == vo.getMove() ? index > 0 : index < sysRoles.size() - 1 && index != -1) {
            SysRole oldSysRole = sysRoles.get(Move.UP == vo.getMove() ? index - 1 : index + 1);
            Long oldSort = oldSysRole.getSort();
            oldSysRole.setSort(sysRole.getSort());
            sysRole.setSort(oldSort);
            this.sysRoleDao.save(sysRole);
            this.sysRoleDao.save(oldSysRole);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(SysRole sysRole) {
        if (Objects.nonNull(this.sysRoleDao.findByName(sysRole.getName()))) {
            throw new WebServerException(ServerResponseEntity.fail("角色名称已存在！"));
        }
        this.sysRoleDao.save(sysRole);
        sysRole.setSort(sysRole.getId());
        this.sysRoleDao.save(sysRole);
    }

    public void update(SysRole sysRole) {
        SysRole oldSysRole = this.sysRoleDao.findById(sysRole.getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("角色不存在！")));
        if (!Objects.equals(oldSysRole.getName(), sysRole.getName())
                && Objects.nonNull(this.sysRoleDao.findByName(sysRole.getName()))) {
            throw new WebServerException(ServerResponseEntity.fail("角色名称已存在！"));
        }
        BeanUtils.copyNonNullProperties(sysRole, oldSysRole);
        this.sysRoleDao.save(oldSysRole);
    }

    public void delete(Long id) {
        SysRole sysRole = this.sysRoleDao.findById(id)
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("角色不存在！")));
        if (this.sysRoleDao.existsByRoleId(sysRole.getId())) {
            throw new WebServerException(ServerResponseEntity.fail("此角色已绑定用户，请先解绑用户下的角色！"));
        }
        this.sysRoleDao.deleteById(id);
    }
}
