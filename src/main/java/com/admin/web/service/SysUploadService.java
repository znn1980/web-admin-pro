package com.admin.web.service;

import com.admin.web.configuration.ConfigProperties;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author znn
 */
@Service
public class SysUploadService {
    private final static String SYS_DOWNLOAD_URL = "/sys/download/";
    private final ConfigProperties.Upload upload;

    public SysUploadService(ConfigProperties configProperties) {
        this.upload = configProperties.getUpload();
    }

    public SysUpload upload(HttpServletRequest request, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new ServerResponseException("上传的文件不存在！");
        }
        if (!this.hasFilename(file.getOriginalFilename())) {
            throw new ServerResponseException("上传的文件中包含无效的字符！");
        }
        if (!this.hasContentType(file.getContentType())) {
            throw new ServerResponseException("上传的文件中包含不支持的格式！");
        }
        String fileName = this.uploadFilename(file);
        if (!Files.exists(this.upload.getLocation().resolve(fileName).getParent())) {
            Files.createDirectories(this.upload.getLocation().resolve(fileName).getParent());
        }
        file.transferTo(this.upload.getLocation().resolve(fileName));
        return new SysUpload(
                String.format("%s%s%s", request.getContextPath(), SYS_DOWNLOAD_URL, fileName),
                StringUtils.getFilename(file.getOriginalFilename())
        );
    }

    public Resource download(HttpServletRequest request) throws NoResourceFoundException {
        String fileName = this.downloadFilename(request);
        Resource resource = new FileSystemResource(this.upload.getLocation().resolve(fileName));
        if (!this.hasFilename(fileName) || !resource.exists()) {
            throw new NoResourceFoundException(HttpMethod.GET, fileName);
        }
        return resource;
    }

    public boolean hasFilename(String fileName) {
        return StringUtils.hasText(fileName) && fileName.length() < 255
                && this.upload.getLocation().resolve(fileName)
                .normalize().startsWith(this.upload.getLocation());
    }

    public boolean hasContentType(String contentType) {
        if (StringUtils.hasLength(contentType)) {
            for (MimeType mimeType : this.upload.getExtensions()) {
                if (mimeType.includes(MimeType.valueOf(contentType))) {
                    return true;
                }
            }
        }
        return false;
    }

    public String uploadFilename(MultipartFile file) {
        return String.format("%s/%s_%s.%s",
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmssSSS")),
                StringUtils.delete(UUID.randomUUID().toString(), "-"),
                StringUtils.getFilenameExtension(file.getOriginalFilename())
        );
    }

    public String downloadFilename(HttpServletRequest request) {
        return URLDecoder.decode(
                StringUtils.delete(request.getRequestURI(), SYS_DOWNLOAD_URL),
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
