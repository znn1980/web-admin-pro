package com.admin.web.controller;

import com.admin.web.model.SysCode;
import com.admin.web.service.SysCodeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * @author znn
 */
@RestController
public class SysCodeController extends BaseController {
    private final SysCodeService sysCodeService;

    public SysCodeController(SysCodeService sysCodeService) {
        this.sysCodeService = sysCodeService;
    }

    @GetMapping("/sys/code.jpg")
    public ResponseEntity<StreamingResponseBody> sysCode() {
        SysCode sysCode = this.sysCodeService.sysCode();
        super.setSysCode(sysCode.code());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.EXPIRES, "0")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, max-age=0")
                .body(os -> this.sysCodeService.write(sysCode, MediaType.IMAGE_JPEG, os));
    }
}
