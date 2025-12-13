package com.admin.web.controller;

import com.admin.web.annotation.SysPermissions;
import com.admin.web.model.*;
import com.admin.web.model.vo.UserLogVo;
import com.admin.web.service.SysUserLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
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
    @PostMapping("/page")
    public ServerResponseEntity<List<SysUserLog>> page(@RequestBody @Validated UserLogVo userLogVo) {
        Page<SysUserLog> sysUserLogs = this.sysUserLogService.findAll(userLogVo
                , PageRequest.of(userLogVo.getPage(), userLogVo.getLimit()));
        return ServerResponseEntity.ok(sysUserLogs.getTotalElements(), sysUserLogs.getContent());
    }

    @SysPermissions
    @PostMapping("/user/page")
    public ServerResponseEntity<List<SysUserLog>> uPage(@RequestBody @Validated UserLogVo userLogVo) {
        userLogVo.setUsername(super.getSysUser().getUsername());
        Page<SysUserLog> sysUserLogs = this.sysUserLogService.findAll(userLogVo
                , PageRequest.of(userLogVo.getPage(), userLogVo.getLimit()));
        return ServerResponseEntity.ok(sysUserLogs.getTotalElements(), sysUserLogs.getContent());
    }

    @SysPermissions
    @DeleteMapping()
    public ServerResponseEntity<?> delete(@RequestBody List<Long> id) {
        this.sysUserLogService.deleteAllById(id);
        return ServerResponseEntity.ok();
    }


}
