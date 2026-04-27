package com.admin.web.aspect;

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
    private static final Logger logger = LoggerFactory.getLogger(SysLicenseAspect.class);

    @Before("login()")
    public void doBefore() {
        byte[] bytes = SysLicense.readSysLicense(Paths.get("license.dat"));
        SysLicense license = SysLicense.asSysLicense(bytes);
        logger.info("SYS-ASPECT => 许可证[编号={}, 有效期=[{}, {}]]", license.num(), license.from(), license.to());
        SysLicense.hasSysLicense(license);
    }

    @Pointcut("execution(* com.admin.web.service.SysUserService.login(..))")
    public void login() {
    }

    @Bean
    public ApplicationRunner runner() {
        String licenseNumber = SysLicense.asSysLicenseNumber();
        logger.info("[许可证] => {}", licenseNumber);
        return args -> Files.write(Paths.get("license.key"), licenseNumber.getBytes());
    }

}
