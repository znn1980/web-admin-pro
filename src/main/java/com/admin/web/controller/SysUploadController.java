package com.admin.web.controller;

import com.admin.web.annotation.SysLog;
import com.admin.web.annotation.SysLogin;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.config.ConfigProperties;
import com.admin.web.model.ServerResponse;
import com.admin.web.model.SysUpload;
import com.google.code.kaptcha.Producer;
import jakarta.servlet.ServletException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author znn
 */
@RestController
public class SysUploadController extends BaseController {
    private static final DateTimeFormatter UPLOAD_PATH = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter UPLOAD_NAME = DateTimeFormatter.ofPattern("HHmmssSSS");
    private final ConfigProperties configProperties;
    private final Producer producer;

    public SysUploadController(ConfigProperties configProperties, Producer producer) {
        this.configProperties = configProperties;
        this.producer = producer;
    }

    @SysLog("上传文件")
    @SysPermissions(SysLogin.class)
    @PostMapping("/sys/upload.json")
    public ServerResponse<SysUpload> upload(MultipartFile file) throws IOException, ServletException {
        if (!this.configProperties.getUpload().hasUpload(file.getContentType())) {
            return ServerResponse.fail("上传的文件中包含不支持的格式！");
        }
        String fileName = String.format("%s/%s.%s"
                , LocalDate.now().format(UPLOAD_PATH)
                , LocalTime.now().format(UPLOAD_NAME)
                , file.getOriginalFilename());
        Files.createDirectories(Paths.get(this.configProperties.getUpload().getLocation(), fileName).getParent());
        file.transferTo(Paths.get(this.configProperties.getUpload().getLocation(), fileName));
        SysUpload sysUpload = new SysUpload();
        sysUpload.setSrc(String.format("%s/sys/download/%s", super.getRequest().getContextPath(), fileName));
        sysUpload.setTitle(file.getOriginalFilename());
        return ServerResponse.ok(sysUpload);
    }

    @GetMapping("/sys/download/**")
    public ResponseEntity<Resource> download() {
        String fileName = URLDecoder.decode(StringUtils
                .delete(super.getRequest().getRequestURI(), "/sys/download/"), StandardCharsets.UTF_8);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\""
                        , URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                                .replace("+", "%20")))
                .body(new FileSystemResource(Paths.get(this.configProperties.getUpload().getLocation(), fileName)));
    }

    @GetMapping("/sys/code.jpg")
    public ResponseEntity<StreamingResponseBody> code() throws IOException {
        String sysCode = this.producer.createText();
        super.setSysCode(sysCode);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.EXPIRES, "0")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, max-age=0")
                .body(os -> ImageIO.write(producer.createImage(sysCode), "jpg", os));
    }

}
