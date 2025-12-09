package com.admin.web.utils;

import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

/**
 * @author znn
 */
public class UserAgentUtils {

    public static UserAgent getUserAgent(HttpServletRequest request) {
        return UserAgent.parseUserAgentString(request.getHeader(HttpHeaders.USER_AGENT));
    }

    public static String getOs(UserAgent userAgent) {
        return userAgent.getOperatingSystem().getName();
    }

    public static String getBrowser(UserAgent userAgent) {
        return userAgent.getBrowser().getName();
    }
}
