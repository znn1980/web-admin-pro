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
        String sysCaptcha = this.producer.createText();
        return new SysCaptcha(sysCaptcha, this.producer.createImage(sysCaptcha));
    }

    public void write(SysCaptcha sysCode, MediaType mediaType, OutputStream os) throws IOException {
        ImageIO.write(sysCode.image(), mediaType.getSubtype(), os);
    }
}
