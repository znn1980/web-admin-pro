package com.admin.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author znn
 */
@Configuration
@ConfigurationProperties(prefix = "config")
public class ConfigProperties {
    private String name = "WEB-ADMIN-PRO";
    private String version = "1.0.0";
    private String copyright = "© 2025 All Rights Reserved.";

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
}
