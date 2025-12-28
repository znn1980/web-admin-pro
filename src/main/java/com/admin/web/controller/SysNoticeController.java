package com.admin.web.controller;

import com.admin.web.annotation.*;
import com.admin.web.exception.WebServerException;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysNotice;
import com.admin.web.model.vo.NoticeVo;
import com.admin.web.model.vo.PageVo;
import com.admin.web.service.SysNoticeService;
import com.admin.web.utils.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author znn
 */
@RestController
@RequestMapping("/sys/notice")
public class SysNoticeController extends BaseController {
    private final SysNoticeService sysNoticeService;

    public SysNoticeController(SysNoticeService sysNoticeService) {
        this.sysNoticeService = sysNoticeService;
    }

    @SysPermissions(SysLogin.class)
    @PostMapping("/all")
    public ServerResponseEntity<List<SysNotice>> all(@RequestBody @Validated NoticeVo noticeVo) {
        noticeVo.setUser(super.getSysUser());
        Page<SysNotice> sysNotices = this.sysNoticeService.findAll(noticeVo, PageVo.of(noticeVo));
        return ServerResponseEntity.ok(sysNotices.getTotalElements(), sysNotices.getContent());
    }

    @SysPermissions(SysLogin.class)
    @PostMapping("/show")
    public ServerResponseEntity<SysNotice> show(@RequestBody Long id) {
        SysNotice sysNotice = this.sysNoticeService.findById(id)
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("通知公告不存在！")));
        if (!Objects.equals(sysNotice.getCreateUsername(), super.getSysUser().getUsername())
                && !super.isSuperAdmin() && sysNotice.isDisable()) {
            return ServerResponseEntity.fail("您不能查看已禁用通知公告！");
        }
        this.sysNoticeService.save(sysNotice, super.getSysUser());
        return ServerResponseEntity.ok(sysNotice);
    }

    @SysLog("创建通知公告")
    @SysPermissions
    @PostMapping("/create")
    public ServerResponseEntity<?> create(@RequestBody @Validated(SysCreate.class) SysNotice sysNotice) {
        this.sysNoticeService.save(sysNotice);
        return ServerResponseEntity.ok();
    }

    @SysLog("修改通知公告")
    @SysPermissions
    @PutMapping("/update")
    public ServerResponseEntity<?> update(@RequestBody @Validated(SysUpdate.class) SysNotice sysNotice) {
        SysNotice oldSysNotice = this.sysNoticeService.findById(sysNotice.getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("通知公告不存在！")));
        if (!super.isSuperAdmin()
                && !Objects.equals(oldSysNotice.getCreateUsername(), super.getSysUser().getUsername())) {
            return ServerResponseEntity.fail("您只能修改自己发布的通知公告！");
        }
        sysNotice.setUsers(null);
        BeanUtils.copyNonNullProperties(sysNotice, oldSysNotice);
        this.sysNoticeService.save(oldSysNotice);
        return ServerResponseEntity.ok();
    }

    @SysLog("删除通知公告")
    @SysPermissions
    @DeleteMapping("/delete")
    public ServerResponseEntity<?> delete(@RequestBody Long id) {
        SysNotice sysNotice = this.sysNoticeService.findById(id)
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("通知公告不存在！")));
        if (!super.isSuperAdmin()
                && !Objects.equals(sysNotice.getCreateUsername(), super.getSysUser().getUsername())) {
            return ServerResponseEntity.fail("您只能删除自己发布的通知公告！");
        }
        this.sysNoticeService.deleteById(id);
        return ServerResponseEntity.ok();
    }
}
