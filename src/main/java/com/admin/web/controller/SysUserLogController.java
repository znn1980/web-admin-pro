package com.admin.web.controller;

import com.admin.web.annotation.SysLogin;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.model.*;
import com.admin.web.model.vo.UserLogVo;
import com.admin.web.service.SysUserLogService;
import org.springframework.data.domain.Page;
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

    @SysPermissions(SysLogin.class)
    @PostMapping("/me.json")
    public ServerResponse<List<SysUserLog>> me(@RequestBody @Validated UserLogVo vo) {
        Page<SysUserLog> logs = this.sysUserLogService.all(vo.username(super.getSysUser().getUsername()));
        return ServerResponse.ok(logs.getTotalElements(), logs.getContent());
    }

    @SysPermissions
    @PostMapping("/all.json")
    public ServerResponse<List<SysUserLog>> all(@RequestBody @Validated UserLogVo vo) {
        Page<SysUserLog> logs = this.sysUserLogService.all(vo);
        return ServerResponse.ok(logs.getTotalElements(), logs.getContent());
    }

    @SysPermissions
    @DeleteMapping("/delete.json")
    public ServerResponse<?> delete(@RequestBody List<Long> id) {
        this.sysUserLogService.delete(id);
        return ServerResponse.ok();
    }

}
