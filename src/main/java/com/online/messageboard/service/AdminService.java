package com.online.messageboard.service;

import com.online.messageboard.entity.Admin;

/**
 * 管理员 Service 接口
 */
public interface AdminService {

    /**
     * 根据用户名和密码查询管理员（登录
     */
    Admin login(String username, String password);

    /**
     * 根据用户名查询管理员
     */
    Admin getByUsername(String username);
}
