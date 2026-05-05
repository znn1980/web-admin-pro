package com.admin.web.service;

import com.admin.web.model.SysCaptcha;
import com.google.code.kaptcha.Producer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author znn
 */
@Service
public class SysCaptchaService {
    private final Producer producer;

    public SysCaptchaService(Producer producer) {
        this.producer = producer;
    }

    public SysCaptcha sysCaptcha() {
        String captcha = this.producer.createText();
        return new SysCaptcha(captcha, this.producer.createImage(captcha), MediaType.IMAGE_JPEG);
    }

    public void write(SysCaptcha sysCaptcha, OutputStream os) throws IOException {
        ImageIO.write(sysCaptcha.image(), sysCaptcha.mediaType().getSubtype(), os);
    }
}
