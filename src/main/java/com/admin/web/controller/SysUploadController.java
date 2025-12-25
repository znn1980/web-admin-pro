package com.admin.web.controller;

import com.admin.web.annotation.SysLog;
import com.admin.web.annotation.SysLogin;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysUpload;
import com.google.code.kaptcha.Producer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author znn
 */
@RestController
public class SysUploadController extends BaseController {
    private static final Path UPLOAD_ROOT = Paths.get("uploads");
    private static final DateTimeFormatter UPLOAD_PATH = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter UPLOAD_NAME = DateTimeFormatter.ofPattern("HHmmssSSS");
    private final Producer producer;

    public SysUploadController(Producer producer) {
        this.producer = producer;
    }

    @SysLog("上传文件")
    @SysPermissions(SysLogin.class)
    @PostMapping("/sys/upload")
    public ServerResponseEntity<SysUpload> upload(HttpServletRequest request, MultipartFile file) throws IOException {
        String fileName = String.format("%s/%s.%s"
                , LocalDate.now().format(UPLOAD_PATH)
                , LocalTime.now().format(UPLOAD_NAME)
                , file.getOriginalFilename());
        Files.createDirectories(UPLOAD_ROOT.resolve(fileName).getParent());
        file.transferTo(UPLOAD_ROOT.resolve(fileName));
        SysUpload sysUpload = new SysUpload();
        sysUpload.setSrc(String.format("%s/sys/download?fileName=%s", request.getContextPath(), fileName));
        sysUpload.setTitle(file.getOriginalFilename());
        return ServerResponseEntity.ok(sysUpload);
    }

    @GetMapping("/sys/download")
    public void download(HttpServletResponse response, String fileName) throws IOException {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;fileName=%s"
                , URLEncoder.encode(fileName, StandardCharsets.UTF_8)));
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        Files.copy(UPLOAD_ROOT.resolve(fileName), response.getOutputStream());
    }

    @GetMapping("/sys/code.jpg")
    public void code(HttpServletResponse response) throws IOException {
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate");
        response.addHeader(HttpHeaders.CACHE_CONTROL, "post-check=0, pre-check=0");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        String sysCode = this.producer.createText();
        super.setSysCode(sysCode);
        ImageIO.write(this.producer.createImage(sysCode), "jpg", response.getOutputStream());
    }
}
