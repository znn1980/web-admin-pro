package com.admin.web.service;

import com.admin.web.model.request.WhereRequest;
import com.admin.web.repository.SysNoticeRepository;
import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.entity.SysNotice;
import com.admin.web.model.entity.SysUser;
import com.admin.web.model.enums.ResponseCode;
import com.admin.web.model.request.NoticeRequest;
import com.admin.web.model.request.PageRequest;
import com.admin.web.utils.BeanUtils;
import com.admin.web.utils.SecurityUtils;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author znn
 */
@Service
public class SysNoticeService {
    private final SysNoticeRepository sysNoticeRepository;

    public SysNoticeService(SysNoticeRepository sysNoticeRepository) {
        this.sysNoticeRepository = sysNoticeRepository;
    }

    public Page<SysNotice> all(NoticeRequest request, SysUser sysUser) {
        return this.sysNoticeRepository.findAll((root, query, builder) ->
                Objects.requireNonNull(query).where(WhereRequest.builder()
                        //未读
                        .add(Objects.equals(NoticeRequest.Status.UNREAD, request.status()), () -> {
                            Subquery<Long> subQuery = query.subquery(Long.class);
                            Root<SysNotice> subRoot = subQuery.from(SysNotice.class);
                            subQuery.select(subRoot.get("id"))
                                    .where(builder.equal(subRoot.join("users").get("id"), sysUser.getId()));
                            return builder.not(root.get("id").in(subQuery));
                        })
                        //已读
                        .add(Objects.equals(NoticeRequest.Status.READ, request.status()), () ->
                                builder.equal(root.join("users").get("id"), sysUser.getId()))
                        //我的
                        .add(Objects.equals(NoticeRequest.Status.ME, request.status()), () ->
                                builder.equal(root.get("createUsername"), sysUser.getUsername()))
                        .add(StringUtils.hasText(request.search()), () -> {
                            String search = String.format("%%%s%%", request.search().toLowerCase());
                            return builder.like(builder.lower(root.get("title")), search);
                        }).build()
                ).orderBy(builder.desc(root.get("createTimestamp"))).getRestriction(), PageRequest.of(request.page(), request.limit(), request.sort()));
    }

    public SysNotice show(Long id, SysUser sysUser) {
        SysNotice sysNotice = this.sysNoticeRepository.findById(id)
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (!Objects.equals(sysNotice.getCreateUsername(), sysUser.getUsername())
                && !SecurityUtils.hasSuperAdmin(sysUser) && sysNotice.isDisable()) {
            throw new ServerResponseException("您不能查看已禁用通知公告！");
        }
        Set<SysUser> sysUsers = sysNotice.getUsers();
        if (Objects.nonNull(sysUsers) && !sysUsers.contains(sysUser)) {
            sysUsers.add(sysUser);
            this.sysNoticeRepository.save(sysNotice);
        }
        return sysNotice;
    }

    public void create(SysNotice sysNotice) {
        this.sysNoticeRepository.save(sysNotice);
    }

    public void update(SysNotice sysNotice, SysUser sysUser) {
        SysNotice oldSysNotice = this.sysNoticeRepository.findById(sysNotice.getId())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (!SecurityUtils.hasSuperAdmin(sysUser)
                && !Objects.equals(oldSysNotice.getCreateUsername(), sysUser.getUsername())) {
            throw new ServerResponseException("您只能修改自己发布的通知公告！");
        }
        sysNotice.setUsers(null);
        oldSysNotice.setUsers(null);
        BeanUtils.copyNonNullProperties(sysNotice, oldSysNotice);
        this.sysNoticeRepository.save(oldSysNotice);
    }

    public void delete(Long id, SysUser sysUser) {
        SysNotice sysNotice = this.sysNoticeRepository.findById(id)
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (!SecurityUtils.hasSuperAdmin(sysUser)
                && !Objects.equals(sysNotice.getCreateUsername(), sysUser.getUsername())) {
            throw new ServerResponseException("您只能删除自己发布的通知公告！");
        }
        this.sysNoticeRepository.deleteById(id);
    }

    public Long unRead(Long userId) {
        return this.sysNoticeRepository.count((root, query, builder) -> {
            Subquery<Long> subQuery = Objects.requireNonNull(query).subquery(Long.class);
            Root<SysNotice> subRoot = subQuery.from(SysNotice.class);
            subQuery.select(subRoot.get("id")).where(builder.equal(subRoot.join("users").get("id"), userId));
            return builder.not(root.get("id").in(subQuery));
        });
    }
}
