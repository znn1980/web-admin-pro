package com.admin.web.service;

import com.admin.web.dao.SysNoticeDao;
import com.admin.web.model.SysNotice;
import com.admin.web.model.SysUser;
import com.admin.web.model.vo.NoticeVo;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
            return this.findUnreadNoticesByUserId(noticeVo.getUser().getId(), page);
        } else if (Objects.equals(NoticeVo.State.READ, noticeVo.getState())) {
            return this.findReadNoticesByUserId(noticeVo.getUser().getId(), page);
        } else if (Objects.equals(NoticeVo.State.ME, noticeVo.getState())) {
            return this.findAllByUsername(noticeVo.getUser().getUsername(), page);
        } else if (Objects.equals(NoticeVo.State.ALL, noticeVo.getState())) {
            return this.findAllByUsername(null, page);
        }
        return this.sysNoticeDao.findAll(page);
    }

    public Page<SysNotice> findAllByUsername(String username, Pageable page) {
        return this.sysNoticeDao.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(username)) {
                predicates.add(builder.equal(root.get("createUsername"), username));
            }
            query.orderBy(builder.desc(root.get("createTimestamp")));
            return builder.and(predicates.toArray(new Predicate[0]));
        }, page);
    }

    public Page<SysNotice> findReadNoticesByUserId(Long userId, Pageable page) {
        return this.sysNoticeDao.findAll((root, query, builder) -> {
            Join<SysNotice, SysUser> join = root.join("users");
            query.orderBy(builder.desc(root.get("createTimestamp")));
            return builder.equal(join.get("id"), userId);
        }, page);
    }

    public Page<SysNotice> findUnreadNoticesByUserId(Long userId, Pageable page) {
        return this.sysNoticeDao.findAll((root, query, builder) -> {
            Subquery<Long> subQuery = query.subquery(Long.class);
            Root<SysNotice> subRoot = subQuery.from(SysNotice.class);
            Join<SysNotice, SysUser> join = subRoot.join("users");
            subQuery.select(subRoot.get("id"))
                    .where(builder.equal(join.get("id"), userId));
            query.orderBy(builder.desc(root.get("createTimestamp")));
            return builder.not(root.get("id").in(subQuery));
        }, page);
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

    public Long countUnreadByUserId(Long userId) {
        return this.sysNoticeDao.countUnreadByUserId(userId);
    }
}
