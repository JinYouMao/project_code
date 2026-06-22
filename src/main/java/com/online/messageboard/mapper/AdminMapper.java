package com.online.messageboard.mapper;

import com.online.messageboard.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 管理员数据访问层接口
 * 定义管理员表的所有数据库操作方法
 */
@Mapper
public interface AdminMapper {

    /**
     * 根据用户名和密码查询管理员
     */
    Admin selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    /**
     * 根据用户名查询管理员
     */
    Admin selectByUsername(String username);
}
