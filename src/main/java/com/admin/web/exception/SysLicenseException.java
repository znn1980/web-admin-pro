package com.admin.web.exception;

/**
 * @author znn
 */
public class SysLicenseException extends RuntimeException {

    public SysLicenseException(String message) {
        super(message);
    }

    public SysLicenseException(String message, Throwable cause) {
        super(message, cause);
    }
}
