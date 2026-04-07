package com.admin.web.controller;

import com.admin.web.annotation.SysLogin;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.config.ConfigProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author znn
 */
@Controller
public class ModelViewController extends BaseController {
    private final ConfigProperties configProperties;

    public ModelViewController(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("ip", super.getClientIp());
        model.addAttribute(ConfigProperties.CONFIG_PREFIX, this.configProperties);
    }

    @GetMapping({"/", "/index.html", "/admin/login.html"})
    public ModelAndView login() {
        return new ModelAndView("admin/login");
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/admin/index.html")
    public ModelAndView index() {
        return new ModelAndView("admin/index");
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/admin/home.html")
    public ModelAndView home() {
        return new ModelAndView("admin/home");
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/admin/me.html")
    public ModelAndView me() {
        return new ModelAndView("admin/me");
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/admin/notice.html")
    public ModelAndView notice() {
        return new ModelAndView("admin/notice");
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/admin/chat.html")
    public ModelAndView chat() {
        return new ModelAndView("admin/chat");
    }

    @SysPermissions
    @GetMapping("/admin/user.html")
    public ModelAndView user() {
        return new ModelAndView("admin/user");
    }

    @SysPermissions
    @GetMapping("/admin/role.html")
    public ModelAndView role() {
        return new ModelAndView("admin/role");
    }

    @SysPermissions
    @GetMapping("/admin/menu.html")
    public ModelAndView menu() {
        return new ModelAndView("admin/menu");
    }

    @SysPermissions
    @GetMapping("/admin/log.html")
    public ModelAndView log() {
        return new ModelAndView("admin/log");
    }

}
