package com.online.messageboard.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 * 对应数据库表 user
 * 支持用户注册与登录功能
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID，主键、自增
     */
    private Integer id;
    /**
     * 用户登录账号，非空、唯一
     */
    private String username;
    /**
     * 用户登录密码，非空
     */
    private String password;
    /**
     * 用户昵称，可空
     */
    private String nickname;
    /**
     * 邮箱，可空
     */
    private String email;
    /**
     * 手机号，可空
     */
    private String phone;
    /**
     * 注册时间，非空
     */
    private Date createTime;
}
