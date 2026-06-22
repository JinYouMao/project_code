package com.online.messageboard.entity;

import java.io.Serializable;

/**
 * 管理员实体类
 * 对应数据库中的admin表
 */
public class Admin implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 管理员ID
     */
    private Integer id;
    
    /**
     * 管理员登录账号
     */
    private String username;
    
    /**
     * 管理员登录密码
     */
    private String password;

    public Admin() {
    }

    public Admin(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
