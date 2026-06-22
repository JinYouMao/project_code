package com.online.messageboard.service;

import com.online.messageboard.entity.Message;

import java.util.List;
import java.util.Map;

/**
 * 留言 Service 接口
 */
public interface MessageService {

    /**
     * 根据ID查询留言（含回复）
     */
    Message getById(Integer id);

    /**
     * 分页查询留言列表
     */
    List<Message> getByPage(int pageNum, int pageSize);

    /**
     * 根据状态分页查询留言
     */
    List<Message> getByStatusAndPage(Integer status, int pageNum, int pageSize);

    /**
     * 获取总记录数
     */
    int countAll();

    /**
     * 根据状态获取记录数
     */
    int countByStatus(Integer status);

    /**
     * 添加留言
     */
    boolean add(Message message);

    /**
     * 更新留言状态
     */
    boolean updateStatus(Integer id, Integer status);

    /**
     * 更新留言分类和违规标记
     */
    boolean updateTypeAndRisk(Integer id, String type, Integer isRisk);

    /**
     * 删除留言
     */
    boolean delete(Integer id);

    /**
     * 批量删除留言
     */
    boolean deleteBatch(List<Integer> ids);

    /**
     * 获取类型统计
     */
    List<Map<String, Object>> getTypeStats();

    /**
     * 获取违规留言数量
     */
    int getRiskCount();

    /**
     * 获取所有留言
     */
    List<Message> getAll();
}
