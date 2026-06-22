package com.online.messageboard.service;

import com.online.messageboard.entity.Reply;

import java.util.List;

/**
 * 回复业务接口
 * 定义回复相关的业务方法
 */
public interface ReplyService {
    
    /**
     * 添加回复
     * @param reply 回复对象
     * @return 是否成功
     */
    boolean addReply(Reply reply);
    
    /**
     * 删除回复
     * @param id 回复ID
     * @return 是否成功
     */
    boolean deleteReply(Integer id);
    
    /**
     * 更新回复
     * @param reply 回复对象
     * @return 是否成功
     */
    boolean updateReply(Reply reply);
    
    /**
     * 根据ID查询回复
     * @param id 回复ID
     * @return 回复对象
     */
    Reply getReplyById(Integer id);
    
    /**
     * 根据留言ID查询回复
     * @param messageId 留言ID
     * @return 回复对象
     */
    Reply getReplyByMessageId(Integer messageId);
    
    /**
     * 查询所有回复
     * @return 回复列表
     */
    List<Reply> getAllReplies();
}
