package com.online.messageboard.controller;

import com.online.messageboard.entity.Admin;
import com.online.messageboard.service.AdminService;
import com.online.messageboard.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员控制器
 * 处理管理员登录和相关请求
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    /**
     * 跳转到登录页
     */
    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }
    
    /**
     * 执行登录
     */
    @PostMapping("/login")
    @ResponseBody
    public void doLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request,
            HttpServletResponse response) {
        
        try {
            // 参数校验
            if (username == null || username.trim().isEmpty()) {
                WebUtil.writeJSON(response, WebUtil.error("用户名不能为空"));
                return;
            }
            
            if (password == null || password.trim().isEmpty()) {
                WebUtil.writeJSON(response, WebUtil.error("密码不能为空"));
                return;
            }
            
            // 执行登录
            Admin admin = adminService.login(username.trim(), password);
            
            if (admin != null) {
                // 登录成功，保存到Session
                WebUtil.setAdminToSession(request, admin);
                
                Map<String, Object> data = new HashMap<>();
                data.put("username", admin.getUsername());
                data.put("message", "登录成功");
                
                WebUtil.writeJSON(response, WebUtil.success("登录成功", data));
            } else {
                WebUtil.writeJSON(response, WebUtil.error("用户名或密码错误"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("登录失败，请稍后重试"));
        }
    }
    
    /**
     * 执行退出
     */
    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            WebUtil.removeAdminFromSession(request);
            response.sendRedirect("/admin/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 检查是否已登录
     */
    @GetMapping("/checkLogin")
    @ResponseBody
    public void checkLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            Admin admin = (Admin) WebUtil.getAdminFromSession(request);
            if (admin != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("loggedIn", true);
                data.put("username", admin.getUsername());
                WebUtil.writeJSON(response, WebUtil.success("已登录", data));
            } else {
                Map<String, Object> data = new HashMap<>();
                data.put("loggedIn", false);
                WebUtil.writeJSON(response, WebUtil.success("未登录", data));
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("检查失败"));
        }
    }
}
