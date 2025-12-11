package com.admin.web.controller;

import com.admin.web.annotation.SysLogin;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.config.WebServerConfig;
import com.google.code.kaptcha.Producer;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * @author znn
 */
@Controller
public class ModelViewController extends BaseController {
    private final Producer producer;
    private final WebServerConfig webServerConfig;

    public ModelViewController(Producer producer, WebServerConfig webServerConfig) {
        this.producer = producer;
        this.webServerConfig = webServerConfig;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("config", this.webServerConfig);
    }

    @GetMapping("/admin/login.html")
    public ModelAndView login() {
        return new ModelAndView("admin/login");
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/admin/index.html")
    public ModelAndView index() {
        return new ModelAndView("admin/index");
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/admin/welcome.html")
    public ModelAndView welcome() {
        return new ModelAndView("admin/welcome");
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/admin/me.html")
    public ModelAndView me() {
        return new ModelAndView("admin/me");
    }

    @SysPermissions()
    @GetMapping("/admin/role.html")
    public ModelAndView role() {
        return new ModelAndView("admin/role");
    }

    @SysPermissions()
    @GetMapping("/admin/log.html")
    public ModelAndView log() {
        return new ModelAndView("admin/log");
    }

    @GetMapping("/sys/code.jpg")
    public void code(HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setDateHeader("Expires", 0L);
        response.setContentType("image/jpeg");
        String sysCode = this.producer.createText();
        super.setSysCode(sysCode);
        ImageIO.write(this.producer.createImage(sysCode), "jpg", response.getOutputStream());
    }
}
