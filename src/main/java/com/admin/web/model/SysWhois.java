package com.admin.web.model;

/**
 * @author znn
 */
public record SysWhois(String ip, String addr,
                       String pro, String proCode,
                       String city, String cityCode,
                       String region, String regionCode) {
}