package com.online.messageboard.interceptor;

import com.online.messageboard.entity.Admin;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 管理员登录拦截器
 * 检查管理员是否已登录，未登录则重定向到登录页面
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {

    /**
     * Session 中存储管理员信息的键名
     */
    public static final String SESSION_ADMIN_KEY = "admin";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Admin admin = (Admin) session.getAttribute(SESSION_ADMIN_KEY);

        if (admin == null) {
            // 未登录，重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }

        return true;
    }
}
