package com.admin.web.controller;

import com.admin.web.annotation.*;
import com.admin.web.model.ServerResponse;
import com.admin.web.model.SysNotice;
import com.admin.web.model.vo.NoticeVo;
import com.admin.web.service.SysNoticeService;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/all.json")
    public ServerResponse<List<SysNotice>> all(@RequestBody @Validated NoticeVo vo) {
        Page<SysNotice> sysNotices = this.sysNoticeService.all(vo, super.getSysUser());
        return ServerResponse.ok(sysNotices.getTotalElements(), sysNotices.getContent());
    }

    @SysPermissions(SysLogin.class)
    @PostMapping("/show.json")
    public ServerResponse<SysNotice> show(@RequestBody Long id) {
        SysNotice sysNotice = this.sysNoticeService.show(id, super.getSysUser());
        return ServerResponse.ok(sysNotice);
    }

    @SysLog("创建通知公告")
    @SysPermissions
    @PostMapping("/create.json")
    public ServerResponse<?> create(@RequestBody @Validated(SysCreate.class) SysNotice sysNotice) {
        this.sysNoticeService.create(sysNotice);
        return ServerResponse.ok();
    }

    @SysLog("修改通知公告")
    @SysPermissions
    @PutMapping("/update.json")
    public ServerResponse<?> update(@RequestBody @Validated(SysUpdate.class) SysNotice sysNotice) {
        this.sysNoticeService.update(sysNotice, super.getSysUser());
        return ServerResponse.ok();
    }

    @SysLog("删除通知公告")
    @SysPermissions
    @DeleteMapping("/delete.json")
    public ServerResponse<?> delete(@RequestBody Long id) {
        this.sysNoticeService.delete(id, super.getSysUser());
        return ServerResponse.ok();
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/unRead.json")
    public ServerResponse<Long> unRead() {
        Long unRead = this.sysNoticeService.unRead(super.getSysUser().getId());
        return ServerResponse.ok(unRead, unRead);
    }
}
