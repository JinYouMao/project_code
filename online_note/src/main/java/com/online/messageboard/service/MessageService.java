package com.online.messageboard.service;

import com.online.messageboard.entity.Message;

import java.util.List;
import java.util.Map;

/**
 * 留言业务接口
 * 定义留言相关的业务方法
 */
public interface MessageService {
    
    /**
     * 提交留言
     * @param message 留言对象
     * @return 是否成功
     */
    boolean addMessage(Message message);
    
    /**
     * 删除留言
     * @param id 留言ID
     * @return 是否成功
     */
    boolean deleteMessage(Integer id);
    
    /**
     * 批量删除留言
     * @param ids 留言ID列表
     * @return 是否成功
     */
    boolean deleteBatch(List<Integer> ids);
    
    /**
     * 更新留言
     * @param message 留言对象
     * @return 是否成功
     */
    boolean updateMessage(Message message);
    
    /**
     * 根据ID查询留言
     * @param id 留言ID
     * @return 留言对象
     */
    Message getMessageById(Integer id);
    
    /**
     * 分页查询留言列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 留言列表
     */
    List<Message> getMessageList(Integer pageNum, Integer pageSize);
    
    /**
     * 根据状态筛选留言
     * @param status 留言状态
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 留言列表
     */
    List<Message> getMessagesByStatus(Integer status, Integer pageNum, Integer pageSize);
    
    /**
     * 查询留言总数
     * @return 总数
     */
    int getTotalCount();
    
    /**
     * 根据状态查询数量
     * @param status 留言状态
     * @return 数量
     */
    int getCountByStatus(Integer status);
    
    /**
     * 获取统计数据
     * @return 统计数据Map
     */
    Map<String, Object> getStatistics();
    
    /**
     * 更新留言状态
     * @param id 留言ID
     * @param status 新状态
     * @return 是否成功
     */
    boolean updateStatus(Integer id, Integer status);
    
    /**
     * AI智能分类留言
     * @param id 留言ID
     * @return 分类结果
     */
    String classifyMessage(Integer id);
    
    /**
     * AI检测留言违规内容
     * @param content 留言内容
     * @return 检测结果（是否违规）
     */
    boolean checkRiskContent(String content);
    
    /**
     * 批量检测留言违规内容
     * @param ids 留言ID列表
     * @return 检测结果
     */
    int batchCheckRisk(List<Integer> ids);
    
    /**
     * AI生成回复内容
     * @param messageId 留言ID
     * @return 生成的回复内容
     */
    String generateReply(Integer messageId);
    
    /**
     * AI润色留言内容
     * @param content 原始内容
     * @return 润色后内容
     */
    String polishContent(String content);
}
