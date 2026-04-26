package com.admin.web.service;

import com.admin.web.config.ConfigProperties;
import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.SysUpload;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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
@Service
public class SysUploadService {
    private static final DateTimeFormatter UPLOAD_PATH = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter UPLOAD_NAME = DateTimeFormatter.ofPattern("HHmmssSSS");
    private final ConfigProperties.Upload upload;

    public SysUploadService(ConfigProperties configProperties) {
        this.upload = configProperties.getUpload();
    }

    public SysUpload upload(HttpServletRequest request, MultipartFile file) throws IOException {
        if (file.isEmpty() || !this.hasUpload(file.getContentType())) {
            throw new ServerResponseException("上传的文件中包含不支持的格式！");
        }
        String fileName = this.uploadFilename(file);
        Files.createDirectories(Paths.get(this.upload.getLocation(), fileName).getParent());
        this.transferTo(file, fileName);
        return new SysUpload(
                String.format("%s/sys/download/%s", request.getContextPath(), fileName),
                file.getOriginalFilename()
        );
    }

    public Resource download(HttpServletRequest request) throws NoResourceFoundException {
        String fileName = this.downloadFilename(request);
        Resource resource = new FileSystemResource(Paths.get(this.upload.getLocation(), fileName));
        if (!resource.exists()) {
            throw new NoResourceFoundException(HttpMethod.GET, fileName);
        }
        return resource;
    }

    public boolean hasUpload(String contentType) {
        if (StringUtils.hasLength(contentType)) {
            for (MimeType mimeType : this.upload.getExtensions()) {
                if (mimeType.includes(MimeType.valueOf(contentType))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void transferTo(MultipartFile file, String fileName) throws IOException {
        file.transferTo(Paths.get(this.upload.getLocation(), fileName));
    }

    public String uploadFilename(MultipartFile file) {
        return String.format("%s/%s_%s",
                LocalDate.now().format(UPLOAD_PATH),
                LocalTime.now().format(UPLOAD_NAME),
                file.getOriginalFilename()
        );
    }

    public String downloadFilename(HttpServletRequest request) {
        return URLDecoder.decode(
                StringUtils.delete(request.getRequestURI(), "/sys/download/"),
                StandardCharsets.UTF_8
        );
    }

    public MediaType probeContentType(Resource resource) throws IOException {
        String contentType = Files.probeContentType(resource.getFile().toPath());
        return StringUtils.hasLength(contentType)
                ? MediaType.valueOf(contentType) : MediaType.APPLICATION_OCTET_STREAM;
    }

    public String probeContentDisposition(Resource resource) throws IOException {
        return String.format("attachment; filename*=UTF-8''%s"
                , URLEncoder.encode(resource.getFile().getName(), StandardCharsets.UTF_8)
                        .replace("+", "%20"));
    }
}
