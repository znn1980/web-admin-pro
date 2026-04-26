package com.admin.web.controller;

import com.admin.web.annotation.SysLog;
import com.admin.web.annotation.SysLogin;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.model.ServerResponse;
import com.admin.web.model.SysUpload;
import com.admin.web.service.SysUploadService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;

/**
 * @author znn
 */
@RestController
public class SysUploadController extends BaseController {
    private final SysUploadService sysUploadService;

    public SysUploadController(SysUploadService sysUploadService) {
        this.sysUploadService = sysUploadService;
    }

    @SysLog("上传文件")
    @SysPermissions(SysLogin.class)
    @PostMapping("/sys/upload.json")
    public ServerResponse<SysUpload> upload(MultipartFile file) throws IOException {
        SysUpload sysUpload = this.sysUploadService.upload(super.getRequest(), file);
        return ServerResponse.ok(sysUpload);
    }

    @GetMapping("/sys/download/**")
    public ResponseEntity<Resource> download() throws IOException, NoResourceFoundException {
        Resource resource = this.sysUploadService.download(super.getRequest());
        return ResponseEntity.ok().contentType(this.sysUploadService.probeContentType(resource))
                .header(HttpHeaders.CONTENT_DISPOSITION, this.sysUploadService.probeContentDisposition(resource))
                .body(resource);
    }
}
