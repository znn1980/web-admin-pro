package com.admin.web.controller;

import com.admin.web.annotation.SysPermissions;
import com.admin.web.model.*;
import com.admin.web.service.SysUserLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author znn
 */
@RestController
@RequestMapping("/sys/log")
public class SysUserLogController extends BaseController {
    private final SysUserLogService sysUserLogService;

    public SysUserLogController(SysUserLogService sysUserLogService) {
        this.sysUserLogService = sysUserLogService;
    }

    @SysPermissions
    @PostMapping("/user/page")
    public ServerResponseEntity<List<SysUserLog>> page(@RequestBody UserLogQuery userLogQuery) {
        SysUser sysUser = super.getSysUser();
        userLogQuery.setUsername(sysUser.getUsername());
        Page<SysUserLog> sysUserLogs = this.sysUserLogService.findAll(userLogQuery
                , PageRequest.of(userLogQuery.getPage(), userLogQuery.getLimit()));
        return ServerResponseEntity.ok(sysUserLogs.getTotalElements(), sysUserLogs.getContent());
    }

    @SysPermissions
    @DeleteMapping()
    public ServerResponseEntity<?> delete(@RequestBody List<Long> id) {
        this.sysUserLogService.deleteAllById(id);
        return ServerResponseEntity.ok();
    }


}
