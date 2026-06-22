package com.online.messageboard.service;

import com.online.messageboard.entity.Reply;

import java.util.List;

/**
 * 回复 Service 接口
 */
public interface ReplyService {

    /**
     * 根据留言ID查询回复列表
     */
    List<Reply> getByMessageId(Integer messageId);

    /**
     * 根据ID查询回复
     */
    Reply getById(Integer id);

    /**
     * 添加回复
     */
    boolean add(Reply reply);

    /**
     * 更新回复
     */
    boolean update(Integer id, String replyContent);

    /**
     * 删除回复
     */
    boolean delete(Integer id);

    /**
     * 根据留言ID删除所有回复
     */
    boolean deleteByMessageId(Integer messageId);
}
