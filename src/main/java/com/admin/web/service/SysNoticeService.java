package com.admin.web.service;

import com.admin.web.dao.SysNoticeDao;
import com.admin.web.model.SysNotice;
import com.admin.web.model.SysUser;
import com.admin.web.model.vo.NoticeVo;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<SysNotice> findAll(NoticeVo noticeVo, Pageable page) {
        if (Objects.equals(NoticeVo.State.UNREAD, noticeVo.getState())) {
            //未读
            return this.sysNoticeDao.findAll((root, query, builder) -> {
                Subquery<Long> subQuery = query.subquery(Long.class);
                Root<SysNotice> subRoot = subQuery.from(SysNotice.class);
                subQuery.select(subRoot.get("id")).where(builder.equal(subRoot.join("users").get("id"), noticeVo.getUser().getId()));
                query.orderBy(builder.desc(root.get("createTimestamp")));
                return builder.not(root.get("id").in(subQuery));
            }, page);
        } else if (Objects.equals(NoticeVo.State.READ, noticeVo.getState())) {
            //已读
            return this.sysNoticeDao.findAll((root, query, builder) -> {
                query.orderBy(builder.desc(root.get("createTimestamp")));
                return builder.equal(root.join("users").get("id"), noticeVo.getUser().getId());
            }, page);
        } else if (Objects.equals(NoticeVo.State.ME, noticeVo.getState())) {
            //我的
            return this.sysNoticeDao.findByCreateUsernameOrderByCreateTimestampDesc(noticeVo.getUser().getUsername(), page);
        } else if (Objects.equals(NoticeVo.State.ALL, noticeVo.getState())) {
            //全部
            return this.sysNoticeDao.findByOrderByCreateTimestampDesc(page);
        }
        return this.sysNoticeDao.findAll(page);
    }

    public Optional<SysNotice> findById(Long id) {
        return this.sysNoticeDao.findById(id);
    }

    public void save(SysNotice sysNotice, SysUser sysUser) {
        Set<SysUser> sysUsers = sysNotice.getUsers();
        if (Objects.nonNull(sysUsers) && !sysUsers.contains(sysUser)) {
            sysUsers.add(sysUser);
            this.sysNoticeDao.save(sysNotice);
        }
    }

    public void save(SysNotice sysNotice) {
        this.sysNoticeDao.save(sysNotice);
    }

    public void deleteById(Long id) {
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
