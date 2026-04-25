package com.admin.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author znn
 */
@ConfigurationProperties(prefix = ConfigProperties.CONFIG_PREFIX)
public class ConfigProperties {
    public static final String CONFIG_PREFIX = "config";
    public static final String DEFAULT_NAME = "WEB-ADMIN-PRO";
    public static final String DEFAULT_VERSION = "1.0.0";
    public static final String DEFAULT_COPYRIGHT = "© 2025 All Rights Reserved.";
    private String name = DEFAULT_NAME;
    private String version = DEFAULT_VERSION;
    private String copyright = DEFAULT_COPYRIGHT;
    private final Upload upload = new Upload();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Upload getUpload() {
        return upload;
    }

    public static class Upload {
        public static final String DEFAULT_LOCATION = "uploads";
        public static final List<MimeType> DEFAULT_EXTENSION = List.of(MimeType.valueOf("*/*"));
        private String location = DEFAULT_LOCATION;
        private List<MimeType> extensions = DEFAULT_EXTENSION;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public List<MimeType> getExtensions() {
            return extensions;
        }

        public void setExtensions(List<MimeType> extensions) {
            this.extensions = extensions;
        }

        public boolean hasUpload(String contentType) {
            if (StringUtils.hasLength(contentType)) {
                for (MimeType mimeType : this.getExtensions()) {
                    if (mimeType.includes(MimeType.valueOf(contentType))) {
                        return true;
                    }
                }
            }
            return false;
        }

    }
}
