package com.admin.web.controller;

import com.admin.web.annotation.SysCreate;
import com.admin.web.annotation.SysLog;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.annotation.SysUpdate;
import com.admin.web.model.entity.SysConfig;
import com.admin.web.model.request.SearchRequest;
import com.admin.web.model.response.ServerResponse;
import com.admin.web.service.SysConfigService;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author znn
 */
@RestController
@RequestMapping("/sys/config")
public class SysConfigController extends BaseController {
    private final SysConfigService sysConfigService;

    public SysConfigController(SysConfigService sysConfigService) {
        this.sysConfigService = sysConfigService;
    }

    @SysPermissions
    @PostMapping("/all.json")
    public ServerResponse<List<SysConfig>> all(@RequestBody @Validated SearchRequest request) {
        Page<SysConfig> sysConfigs = this.sysConfigService.all(request);
        return ServerResponse.ok(sysConfigs.getTotalElements(), sysConfigs.getContent());
    }

    @SysLog("创建参数")
    @SysPermissions
    @PostMapping("/create.json")
    public ServerResponse<Void> create(@RequestBody @Validated(SysCreate.class) SysConfig sysConfig) {
        this.sysConfigService.create(sysConfig);
        return ServerResponse.ok();
    }

    @SysLog("修改参数")
    @SysPermissions
    @PutMapping("/update.json")
    public ServerResponse<Void> update(@RequestBody @Validated(SysUpdate.class) SysConfig sysConfig) {
        this.sysConfigService.update(sysConfig);
        return ServerResponse.ok();
    }

    @SysLog("删除参数")
    @SysPermissions
    @DeleteMapping("/delete.json")
    public ServerResponse<Void> delete(@RequestBody List<Long> id) {
        this.sysConfigService.delete(id);
        return ServerResponse.ok();
    }
}
