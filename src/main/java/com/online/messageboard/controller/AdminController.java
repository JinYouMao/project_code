package com.online.messageboard.controller;

import com.online.messageboard.entity.Admin;
import com.online.messageboard.interceptor.AdminInterceptor;
import com.online.messageboard.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * 管理员 Controller
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 跳转到登录页面
     */
    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    /**
     * 登录处理
     */
    @PostMapping("/doLogin")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {
        Admin admin = adminService.login(username, password);
        if (admin != null) {
            session.setAttribute(AdminInterceptor.SESSION_ADMIN_KEY, admin);
            return "redirect:/manage/index";
        } else {
            model.addAttribute("error", "用户名或密码错误");
            return "admin/login";
        }
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
