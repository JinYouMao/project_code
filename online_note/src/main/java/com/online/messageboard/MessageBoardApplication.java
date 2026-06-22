package com.online.messageboard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot启动类
 * 在线留言板管理系统
 */
@SpringBootApplication
@MapperScan("com.online.messageboard.mapper")
public class MessageBoardApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MessageBoardApplication.class, args);
        System.out.println("===========================================");
        System.out.println("  在线留言板管理系统已成功启动！");
        System.out.println("  访问地址：http://localhost:8080/user/index");
        System.out.println("  管理后台：http://localhost:8080/admin/login");
        System.out.println("===========================================");
    }
}
