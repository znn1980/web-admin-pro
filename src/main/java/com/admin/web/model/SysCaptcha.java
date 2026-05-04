package com.admin.web.model;

import java.awt.image.BufferedImage;

/**
 * @author znn
 */
public record SysCaptcha(String captcha, BufferedImage image) {
}
