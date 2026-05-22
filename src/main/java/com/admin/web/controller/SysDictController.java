package com.admin.web.controller;

import com.admin.web.annotation.SysCreate;
import com.admin.web.annotation.SysLog;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.annotation.SysUpdate;
import com.admin.web.model.entity.SysDict;
import com.admin.web.model.request.SearchRequest;
import com.admin.web.model.response.ServerResponse;
import com.admin.web.service.SysDictService;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author znn
 */
@RestController
@RequestMapping("/sys/dict")
public class SysDictController extends BaseController {
    private final SysDictService sysDictService;

    public SysDictController(SysDictService sysDictService) {
        this.sysDictService = sysDictService;
    }

    @SysPermissions
    @PostMapping("/all.json")
    public ServerResponse<List<SysDict>> all(@RequestBody @Validated SearchRequest request) {
        Page<SysDict> sysDict = this.sysDictService.all(request);
        return ServerResponse.ok(sysDict.getTotalElements(), sysDict.getContent());
    }

    @SysLog("创建字典")
    @SysPermissions
    @PostMapping("/create.json")
    public ServerResponse<Void> create(@RequestBody @Validated(SysCreate.class) SysDict sysDict) {
        this.sysDictService.create(sysDict);
        return ServerResponse.ok();
    }

    @SysLog("修改字典")
    @SysPermissions
    @PutMapping("/update.json")
    public ServerResponse<Void> update(@RequestBody @Validated(SysUpdate.class) SysDict sysDict) {
        this.sysDictService.update(sysDict);
        return ServerResponse.ok();
    }

    @SysLog("删除字典")
    @SysPermissions
    @DeleteMapping("/delete.json")
    public ServerResponse<Void> delete(@RequestBody List<Long> id) {
        this.sysDictService.delete(id);
        return ServerResponse.ok();
    }
}
