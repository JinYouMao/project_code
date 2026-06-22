package com.online.messageboard.mapper;

import com.online.messageboard.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 管理员Mapper接口
 * 提供管理员相关的数据库操作方法
 */
@Mapper
public interface AdminMapper {
    
    /**
     * 根据用户名和密码查询管理员
     * @param username 用户名
     * @param password 密码
     * @return 管理员对象
     */
    @Select("SELECT * FROM admin WHERE username = #{username} AND password = #{password}")
    Admin selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
    
    /**
     * 根据用户名查询管理员
     * @param username 用户名
     * @return 管理员对象
     */
    @Select("SELECT * FROM admin WHERE username = #{username}")
    Admin selectByUsername(@Param("username") String username);
}
