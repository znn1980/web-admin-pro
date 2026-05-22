package com.admin.web.controller;

import com.admin.web.annotation.SysCreate;
import com.admin.web.annotation.SysLog;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.annotation.SysUpdate;
import com.admin.web.model.entity.SysDictDetail;
import com.admin.web.model.request.MoveRequest;
import com.admin.web.model.response.ServerResponse;
import com.admin.web.service.SysDictDetailService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author znn
 */
@RestController
@RequestMapping("/sys/dict/detail")
public class SysDictDetailController extends BaseController {
    private final SysDictDetailService sysDictDetailService;

    public SysDictDetailController(SysDictDetailService sysDictDetailService) {
        this.sysDictDetailService = sysDictDetailService;
    }

    @SysPermissions
    @PostMapping("/all.json")
    public ServerResponse<List<SysDictDetail>> all(@RequestParam Long id) {
        List<SysDictDetail> sysDictDetails = this.sysDictDetailService.all(id);
        return ServerResponse.ok(sysDictDetails);
    }

    @SysLog("移动字典")
    @SysPermissions
    @PutMapping("/move.json")
    public ServerResponse<Void> move(@RequestBody MoveRequest request) {
        this.sysDictDetailService.move(request);
        return ServerResponse.ok();
    }

    @SysLog("创建字典")
    @SysPermissions
    @PostMapping("/create.json")
    public ServerResponse<Void> create(@RequestBody @Validated(SysCreate.class) SysDictDetail sysDictDetail) {
        this.sysDictDetailService.create(sysDictDetail);
        return ServerResponse.ok();
    }

    @SysLog("修改字典")
    @SysPermissions
    @PutMapping("/update.json")
    public ServerResponse<Void> update(@RequestBody @Validated(SysUpdate.class) SysDictDetail sysDictDetail) {
        this.sysDictDetailService.update(sysDictDetail);
        return ServerResponse.ok();
    }

    @SysLog("删除字典")
    @SysPermissions
    @DeleteMapping("/delete.json")
    public ServerResponse<Void> delete(@RequestBody List<Long> id) {
        this.sysDictDetailService.delete(id);
        return ServerResponse.ok();
    }
}
