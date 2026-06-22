package com.online.messageboard.service.impl;

import com.online.messageboard.entity.Message;
import com.online.messageboard.mapper.MessageMapper;
import com.online.messageboard.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 留言 Service 实现类
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public Message getById(Integer id) {
        return messageMapper.selectById(id);
    }

    @Override
    public List<Message> getByPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return messageMapper.selectByPage(offset, pageSize);
    }

    @Override
    public List<Message> getByStatusAndPage(Integer status, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return messageMapper.selectByStatusAndPage(status, offset, pageSize);
    }

    @Override
    public int countAll() {
        return messageMapper.countAll();
    }

    @Override
    public int countByStatus(Integer status) {
        return messageMapper.countByStatus(status);
    }

    @Override
    public boolean add(Message message) {
        return messageMapper.insert(message) > 0;
    }

    @Override
    public boolean updateStatus(Integer id, Integer status) {
        return messageMapper.updateStatusById(id, status) > 0;
    }

    @Override
    public boolean updateTypeAndRisk(Integer id, String type, Integer isRisk) {
        return messageMapper.updateTypeAndRiskById(id, type, isRisk) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return messageMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deleteBatch(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return messageMapper.deleteBatchByIds(ids) > 0;
    }

    @Override
    public List<Map<String, Object>> getTypeStats() {
        return messageMapper.countByType();
    }

    @Override
    public int getRiskCount() {
        return messageMapper.countRisk();
    }

    @Override
    public List<Message> getAll() {
        return messageMapper.selectAll();
    }
}
