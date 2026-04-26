package com.admin.web.service;

import com.admin.web.model.SysCode;
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
public class SysCodeService {
    private final Producer producer;

    public SysCodeService(Producer producer) {
        this.producer = producer;
    }

    public SysCode sysCode() {
        String sysCode = this.producer.createText();
        return new SysCode(sysCode, this.producer.createImage(sysCode));
    }

    public void write(SysCode sysCode, MediaType mediaType, OutputStream os) throws IOException {
        ImageIO.write(sysCode.image(), mediaType.getSubtype(), os);
    }
}
