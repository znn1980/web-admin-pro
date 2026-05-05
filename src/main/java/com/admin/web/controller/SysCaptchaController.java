package com.admin.web.controller;

import com.admin.web.model.SysCaptcha;
import com.admin.web.service.SysCaptchaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * @author znn
 */
@RestController
public class SysCaptchaController extends BaseController {
    private final SysCaptchaService sysCaptchaService;

    public SysCaptchaController(SysCaptchaService sysCaptchaService) {
        this.sysCaptchaService = sysCaptchaService;
    }

    @GetMapping("/sys/captcha.jpg")
    public ResponseEntity<StreamingResponseBody> sysCaptcha() {
        SysCaptcha sysCaptcha = this.sysCaptchaService.sysCaptcha();
        super.setSysCaptcha(sysCaptcha.captcha());
        return ResponseEntity.ok().contentType(sysCaptcha.mediaType())
                .header(HttpHeaders.EXPIRES, "0")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, max-age=0")
                .body(os -> this.sysCaptchaService.write(sysCaptcha, os));
    }
}
