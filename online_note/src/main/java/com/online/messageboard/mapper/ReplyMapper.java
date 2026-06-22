package com.online.messageboard.mapper;

import com.online.messageboard.entity.Reply;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 回复Mapper接口
 * 提供回复相关的数据库操作方法
 */
@Mapper
public interface ReplyMapper {
    
    /**
     * 插入回复
     * @param reply 回复对象
     * @return 影响行数
     */
    @Insert("INSERT INTO reply (message_id, reply_content, reply_time, admin_name) VALUES (#{messageId}, #{replyContent}, #{replyTime}, #{adminName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Reply reply);
    
    /**
     * 根据ID删除回复
     * @param id 回复ID
     * @return 影响行数
     */
    @Delete("DELETE FROM reply WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据留言ID删除回复
     * @param messageId 留言ID
     * @return 影响行数
     */
    @Delete("DELETE FROM reply WHERE message_id = #{messageId}")
    int deleteByMessageId(@Param("messageId") Integer messageId);
    
    /**
     * 更新回复
     * @param reply 回复对象
     * @return 影响行数
     */
    @Update("UPDATE reply SET reply_content = #{replyContent}, reply_time = #{replyTime} WHERE id = #{id}")
    int update(Reply reply);
    
    /**
     * 根据ID查询回复
     * @param id 回复ID
     * @return 回复对象
     */
    @Select("SELECT * FROM reply WHERE id = #{id}")
    @Results({
        @Result(property = "messageId", column = "message_id"),
        @Result(property = "replyContent", column = "reply_content"),
        @Result(property = "replyTime", column = "reply_time"),
        @Result(property = "adminName", column = "admin_name")
    })
    Reply selectById(@Param("id") Integer id);
    
    /**
     * 根据留言ID查询回复
     * @param messageId 留言ID
     * @return 回复对象
     */
    @Select("SELECT * FROM reply WHERE message_id = #{messageId}")
    @Results({
        @Result(property = "messageId", column = "message_id"),
        @Result(property = "replyContent", column = "reply_content"),
        @Result(property = "replyTime", column = "reply_time"),
        @Result(property = "adminName", column = "admin_name")
    })
    Reply selectByMessageId(@Param("messageId") Integer messageId);
    
    /**
     * 查询所有回复
     * @return 回复列表
     */
    @Select("SELECT * FROM reply ORDER BY reply_time DESC")
    @Results({
        @Result(property = "messageId", column = "message_id"),
        @Result(property = "replyContent", column = "reply_content"),
        @Result(property = "replyTime", column = "reply_time"),
        @Result(property = "adminName", column = "admin_name")
    })
    List<Reply> selectAll();
}
