package com.online.messageboard.mapper;

import com.online.messageboard.entity.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 回复数据访问层接口
 * 定义回复表的所有数据库操作方法
 */
@Mapper
public interface ReplyMapper {

    /**
     * 根据留言ID查询回复列表
     */
    List<Reply> selectByMessageId(Integer messageId);

    /**
     * 根据ID查询单条回复
     */
    Reply selectById(Integer id);

    /**
     * 插入新回复
     */
    int insert(Reply reply);

    /**
     * 根据ID更新回复内容
     */
    int updateById(@Param("id") Integer id, @Param("replyContent") String replyContent, @Param("replyTime") java.util.Date replyTime);

    /**
     * 根据ID删除回复
     */
    int deleteById(Integer id);

    /**
     * 根据留言ID删除所有关联回复
     */
    int deleteByMessageId(Integer messageId);
}
