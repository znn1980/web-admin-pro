package com.admin.web.config;

import com.admin.web.model.SysMonitor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author znn
 */
@Configuration
public class WebMvcConfig {

    @Bean
    public SysMonitor sysMonitor() {
        return new SysMonitor();
    }
}
