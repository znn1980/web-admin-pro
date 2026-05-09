package com.admin.web.controller;

import com.admin.web.annotation.SysLogin;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.model.SysWhois;
import com.admin.web.model.response.ServerResponse;
import com.admin.web.service.SysWhoisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author znn
 */
@RestController
public class SysWhoisController extends BaseController {
    private final SysWhoisService sysWhoisService;

    public SysWhoisController(SysWhoisService sysWhoisService) {
        this.sysWhoisService = sysWhoisService;
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/sys/whois")
    public ServerResponse<SysWhois> whois(@RequestParam(required = false) String ip) {
        SysWhois sysWhois = this.sysWhoisService.whois(ip);
        return ServerResponse.ok(sysWhois);
    }
}
