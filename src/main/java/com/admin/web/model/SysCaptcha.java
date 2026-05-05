package com.admin.web.model;

import org.springframework.http.MediaType;

import java.awt.image.BufferedImage;

/**
 * @author znn
 */
public record SysCaptcha(String captcha, BufferedImage image, MediaType mediaType) {
}
