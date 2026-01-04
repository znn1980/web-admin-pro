package com.admin.web.service;

import com.admin.web.dao.SysNoticeDao;
import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.SysNotice;
import com.admin.web.model.SysUser;
import com.admin.web.model.vo.NoticeVo;
import com.admin.web.model.vo.PageVo;
import com.admin.web.utils.BeanUtils;
import com.admin.web.utils.SecurityUtils;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author znn
 */
@Service
public class SysNoticeService {
    private final SysNoticeDao sysNoticeDao;

    public SysNoticeService(SysNoticeDao sysNoticeDao) {
        this.sysNoticeDao = sysNoticeDao;
    }

    public Page<SysNotice> all(NoticeVo vo, SysUser sysUser) {
        if (Objects.equals(NoticeVo.State.UNREAD, vo.getState())) {
            //未读
            return this.sysNoticeDao.findAll((root, query, builder) -> {
                Subquery<Long> subQuery = query.subquery(Long.class);
                Root<SysNotice> subRoot = subQuery.from(SysNotice.class);
                subQuery.select(subRoot.get("id")).where(builder.equal(subRoot.join("users").get("id"), sysUser.getId()));
                query.orderBy(builder.desc(root.get("createTimestamp")));
                return builder.not(root.get("id").in(subQuery));
            }, PageVo.of(vo));
        } else if (Objects.equals(NoticeVo.State.READ, vo.getState())) {
            //已读
            return this.sysNoticeDao.findAll((root, query, builder) -> {
                query.orderBy(builder.desc(root.get("createTimestamp")));
                return builder.equal(root.join("users").get("id"), sysUser.getId());
            }, PageVo.of(vo));
        } else if (Objects.equals(NoticeVo.State.ME, vo.getState())) {
            //我的
            return this.sysNoticeDao.findByCreateUsernameOrderByCreateTimestampDesc(sysUser.getUsername(), PageVo.of(vo));
        } else if (Objects.equals(NoticeVo.State.ALL, vo.getState())) {
            //全部
            return this.sysNoticeDao.findByOrderByCreateTimestampDesc(PageVo.of(vo));
        }
        return this.sysNoticeDao.findAll(PageVo.of(vo));
    }

    public SysNotice show(Long id, SysUser sysUser) {
        SysNotice sysNotice = this.sysNoticeDao.findById(id)
                .orElseThrow(() -> new ServerResponseException("通知公告不存在！"));
        if (!Objects.equals(sysNotice.getCreateUsername(), sysUser.getUsername())
                && !SecurityUtils.isSuperAdmin(sysUser) && sysNotice.isDisable()) {
            throw new ServerResponseException("您不能查看已禁用通知公告！");
        }
        Set<SysUser> sysUsers = sysNotice.getUsers();
        if (Objects.nonNull(sysUsers) && !sysUsers.contains(sysUser)) {
            sysUsers.add(sysUser);
            this.sysNoticeDao.save(sysNotice);
        }
        return sysNotice;
    }

    public void create(SysNotice sysNotice) {
        this.sysNoticeDao.save(sysNotice);
    }

    public void update(SysNotice sysNotice, SysUser sysUser) {
        SysNotice oldSysNotice = this.sysNoticeDao.findById(sysNotice.getId())
                .orElseThrow(() -> new ServerResponseException("通知公告不存在！"));
        if (!SecurityUtils.isSuperAdmin(sysUser)
                && !Objects.equals(oldSysNotice.getCreateUsername(), sysUser.getUsername())) {
            throw new ServerResponseException("您只能修改自己发布的通知公告！");
        }
        sysNotice.setUsers(null);
        oldSysNotice.setUsers(null);
        BeanUtils.copyNonNullProperties(sysNotice, oldSysNotice);
        this.sysNoticeDao.save(oldSysNotice);
    }

    public void delete(Long id, SysUser sysUser) {
        SysNotice sysNotice = this.sysNoticeDao.findById(id)
                .orElseThrow(() -> new ServerResponseException("通知公告不存在！"));
        if (!SecurityUtils.isSuperAdmin(sysUser)
                && !Objects.equals(sysNotice.getCreateUsername(), sysUser.getUsername())) {
            throw new ServerResponseException("您只能删除自己发布的通知公告！");
        }
        this.sysNoticeDao.deleteById(id);
    }

    public Long countByUserId(Long userId) {
        return this.sysNoticeDao.count((root, query, builder) -> {
            Subquery<Long> subQuery = query.subquery(Long.class);
            Root<SysNotice> subRoot = subQuery.from(SysNotice.class);
            subQuery.select(subRoot.get("id")).where(builder.equal(subRoot.join("users").get("id"), userId));
            return builder.not(root.get("id").in(subQuery));
        });
    }
}
