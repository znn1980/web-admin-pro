package com.admin.web.aspect;

import com.admin.web.exception.ServerResponseException;
import com.admin.web.exception.SysLicenseException;
import com.admin.web.model.SysLicense;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author znn
 */
@Aspect
@Component
public class SysLicenseAspect {
    private static final Logger log = LoggerFactory.getLogger(SysLicenseAspect.class);

    @Before("login()")
    public void doBefore() {
        try {
            byte[] bytes = SysLicense.readSysLicense(Paths.get("key.lic"));
            SysLicense sysLicense = SysLicense.asSysLicense(bytes);
            log.info("SYS-ASPECT => {}", sysLicense);
            SysLicense.hasSysLicense(sysLicense);
        } catch (SysLicenseException e) {
            throw new ServerResponseException(e.getMessage());
        }
    }

    @Pointcut("execution(* com.admin.web.service.SysUserService.login(..))")
    public void login() {
    }

    @Bean
    public ApplicationRunner runner() {
        String sysLicenseNumber = SysLicense.asSysLicenseNumber();
        log.info("许可证编号 => {}", sysLicenseNumber);
        return args -> Files.write(Paths.get("key.txt"), sysLicenseNumber.getBytes());
    }

}
