package com.online.messageboard.service;

import com.online.messageboard.entity.User;

/**
 * 用户 Service 接口
 */
public interface UserService {

    /**
     * 根据ID查询用户
     */
    User getById(Integer id);

    /**
     * 根据用户名查询用户
     */
    User getByUsername(String username);

    /**
     * 用户登录
     */
    User login(String username, String password);

    /**
     * 用户注册
     */
    boolean register(User user);
}
