package com.admin.web.controller;

import com.admin.web.service.SysNoticeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
