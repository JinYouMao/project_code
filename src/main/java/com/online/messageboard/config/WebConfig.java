package com.online.messageboard.config;

import com.online.messageboard.interceptor.AdminInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置类
 * 注册拦截器等 Web 相关配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册管理员登录拦截器
        // 拦截 /manage/** 路径下的所有请求
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/manage/**")
                .excludePathPatterns("/admin/login", "/admin/doLogin", "/admin/logout");
    }
}
