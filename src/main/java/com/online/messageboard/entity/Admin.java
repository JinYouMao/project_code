package com.online.messageboard.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理员实体类
 * 对应数据库表 admin
 */
@Data
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员ID，主键、自增
     */
    private Integer id;
    /**
     * 管理员登录账号，非空、唯一
     */
    private String username;
    /**
     * 管理员登录密码，非空
     */
    private String password;
}
