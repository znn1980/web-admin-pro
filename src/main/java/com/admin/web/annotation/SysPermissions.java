package com.admin.web.annotation;

import java.lang.annotation.*;

/**
 * @author znn
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysPermissions {
    Class<?>[] value() default {};
}
