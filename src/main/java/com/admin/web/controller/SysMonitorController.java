package com.admin.web.controller;

import com.admin.web.annotation.SysLogin;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.model.ServerResponse;
import com.admin.web.model.SysMonitor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author znn
 */
@RestController
public class SysMonitorController {
    private final SysMonitor sysMonitor;

    public SysMonitorController(SysMonitor sysMonitor) {
        this.sysMonitor = sysMonitor;
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/sys/monitor")
    public ServerResponse<SysMonitor> monitor() {
        return ServerResponse.ok(this.sysMonitor);
    }
}
