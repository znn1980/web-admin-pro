package com.admin.web.config;

import com.admin.web.model.SysMonitor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author znn
 */
@EnableConfigurationProperties({ConfigProperties.class})
@Configuration
public class AutoConfig {

    @Bean
    public SysMonitor sysMonitor() {
        return new SysMonitor();
    }
}
