package com.online.messageboard.service.impl;

import com.online.messageboard.entity.Admin;
import com.online.messageboard.mapper.AdminMapper;
import com.online.messageboard.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理员业务实现类
 * 实现管理员相关的业务逻辑
 */
@Service
public class AdminServiceImpl implements AdminService {
    
    @Autowired
    private AdminMapper adminMapper;
    
    @Override
    public Admin login(String username, String password) {
        try {
            return adminMapper.selectByUsernameAndPassword(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean existsByUsername(String username) {
        try {
            return adminMapper.selectByUsername(username) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
