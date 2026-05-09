package com.admin.web.utils;

import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author znn
 */
public class WebUtils extends org.springframework.web.util.WebUtils {
    private WebUtils() {
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        for (String sub : Objects.requireNonNullElse(StringUtils.split(ip, ","), new String[]{ip})) {
            if (StringUtils.hasText(sub.trim()) && !"unknown".equalsIgnoreCase(sub.trim())) {
                ip = sub.trim();
                break;
            }
        }
        return (Objects.equals("0:0:0:0:0:0:0:1", ip) || Objects.equals("::1", ip)) ? "127.0.0.1" : ip;
    }

    public static boolean hasRestRequest(HttpServletRequest request) {
        return StringUtils.startsWithIgnoreCase(request.getHeader(HttpHeaders.ACCEPT)
                , MediaType.APPLICATION_JSON_VALUE)
                || StringUtils.startsWithIgnoreCase(request.getHeader(HttpHeaders.CONTENT_TYPE)
                , MediaType.APPLICATION_JSON_VALUE);
    }

    public static void setSession(HttpServletRequest request, String name, Object value) {
        setSessionAttribute(request, name, value);
    }

    public static <T> T getSession(HttpServletRequest request, String name, Class<T> clazz) {
        return clazz.cast(getSessionAttribute(request, name));
    }

    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static MediaType getContentType(HttpHeaders httpHeaders) {
        return getContentType(httpHeaders, MediaType.APPLICATION_JSON);
    }

    public static MediaType getContentType(HttpHeaders httpHeaders, MediaType defaultMediaType) {
        return Objects.requireNonNullElse(httpHeaders.getContentType(), defaultMediaType);
    }

    public static Charset getCharset(MediaType mediaType) {
        return getCharset(mediaType, StandardCharsets.UTF_8);
    }

    public static Charset getCharset(MediaType mediaType, Charset defaultCharset) {
        return Objects.requireNonNullElse(mediaType.getCharset(), defaultCharset);
    }

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
