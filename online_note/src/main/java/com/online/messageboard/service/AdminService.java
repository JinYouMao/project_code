package com.online.messageboard.service;

import com.online.messageboard.entity.Admin;

/**
 * 管理员业务接口
 * 定义管理员相关的业务方法
 */
public interface AdminService {
    
    /**
     * 管理员登录
     * @param username 用户名
     * @param password 密码
     * @return 管理员对象（如果登录成功）
     */
    Admin login(String username, String password);
    
    /**
     * 检查管理员是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
}
