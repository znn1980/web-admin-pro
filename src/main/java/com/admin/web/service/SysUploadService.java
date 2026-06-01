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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author znn
 */
@Service
public class SysUploadService {
    private final static String SYS_DOWNLOAD_URL = "/sys/download/";
    private final Path location;
    private final List<MimeType> extensions;

    public SysUploadService(ConfigProperties configProperties) {
        this.location = Paths.get(configProperties.getUpload().getLocation());
        this.extensions = configProperties.getUpload().getExtensions();
    }

    public SysUpload upload(HttpServletRequest request, MultipartFile file) throws IOException {
        if (file.isEmpty() || !this.hasFilename(file.getOriginalFilename())) {
            throw new ServerResponseException("上传的文件中包含无效的文件路径！");
        }
        if (file.isEmpty() || !this.hasContentType(file.getContentType())) {
            throw new ServerResponseException("上传的文件中包含不支持的格式！");
        }
        String fileName = this.uploadFilename(file);
        Files.createDirectories(this.location.resolve(fileName).getParent());
        file.transferTo(this.location.resolve(fileName));
        return new SysUpload(
                String.format("%s%s%s", request.getContextPath(), SYS_DOWNLOAD_URL, fileName),
                file.getOriginalFilename()
        );
    }

    public Resource download(HttpServletRequest request) throws NoResourceFoundException {
        String fileName = this.downloadFilename(request);
        Resource resource = new FileSystemResource(this.location.resolve(fileName));
        if (!this.hasFilename(fileName) || !resource.exists()) {
            throw new NoResourceFoundException(HttpMethod.GET, fileName);
        }
        return resource;
    }

    public boolean hasFilename(String fileName) {
        return this.location.resolve(fileName).normalize().startsWith(this.location);
    }

    public boolean hasContentType(String contentType) {
        if (StringUtils.hasLength(contentType)) {
            for (MimeType mimeType : this.extensions) {
                if (mimeType.includes(MimeType.valueOf(contentType))) {
                    return true;
                }
            }
        }
        return false;
    }

    public String uploadFilename(MultipartFile file) {
        return String.format("%s/%s_%s",
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmssSSS")),
                file.getOriginalFilename()
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
