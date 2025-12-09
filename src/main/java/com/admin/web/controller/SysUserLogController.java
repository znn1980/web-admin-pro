package com.admin.web.controller;

import com.admin.web.annotation.SysPermissions;
import com.admin.web.model.PageQuery;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysUser;
import com.admin.web.model.SysUserLog;
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
    public ServerResponseEntity<List<SysUserLog>> page(@RequestBody PageQuery pageQuery) {
        SysUser sysUser = super.getSysUser();
        Page<SysUserLog> sysUserLogs = this.sysUserLogService.findByUsernameOrderByTimestampDesc(sysUser.getUsername()
                , PageRequest.of(pageQuery.getPage(), pageQuery.getLimit()));
        return ServerResponseEntity.ok(sysUserLogs.getTotalElements(), sysUserLogs.getContent());
    }

    @SysPermissions
    @DeleteMapping()
    public ServerResponseEntity<?> delete(@RequestBody List<Long> id) {
        this.sysUserLogService.deleteAllById(id);
        return ServerResponseEntity.ok();
    }


}
