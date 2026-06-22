package com.online.messageboard.service.impl;

import com.online.messageboard.entity.Message;
import com.online.messageboard.entity.Reply;
import com.online.messageboard.mapper.MessageMapper;
import com.online.messageboard.mapper.ReplyMapper;
import com.online.messageboard.service.MessageService;
import com.online.messageboard.util.TongyiAIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 留言业务实现类
 * 实现留言相关的业务逻辑
 */
@Service
public class MessageServiceImpl implements MessageService {
    
    @Autowired
    private MessageMapper messageMapper;
    
    @Autowired
    private ReplyMapper replyMapper;
    
    @Autowired
    private TongyiAIUtil tongyiAIUtil;
    
    @Override
    @Transactional
    public boolean addMessage(Message message) {
        // 设置默认值
        if (message.getUsername() == null || message.getUsername().trim().isEmpty()) {
            message.setUsername("匿名用户");
        }
        if (message.getCreateTime() == null) {
            message.setCreateTime(new Date());
        }
        if (message.getStatus() == null) {
            message.setStatus(0);
        }
        if (message.getType() == null) {
            message.setType("未知");
        }
        if (message.getIsRisk() == null) {
            message.setIsRisk(0);
        }
        
        // AI预检内容风险
        if (tongyiAIUtil.checkRiskContent(message.getContent())) {
            message.setIsRisk(1);
        }
        
        // AI自动分类
        String type = tongyiAIUtil.classifyContent(message.getContent());
        message.setType(type);
        
        return messageMapper.insert(message) > 0;
    }
    
    @Override
    @Transactional
    public boolean deleteMessage(Integer id) {
        try {
            // 先删除关联的回复
            replyMapper.deleteByMessageId(id);
            return messageMapper.deleteById(id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean deleteBatch(List<Integer> ids) {
        try {
            // 先批量删除关联的回复
            for (Integer id : ids) {
                replyMapper.deleteByMessageId(id);
            }
            return messageMapper.deleteBatch(ids) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean updateMessage(Message message) {
        try {
            // 如果内容变更，重新检测风险
            if (message.getContent() != null) {
                if (tongyiAIUtil.checkRiskContent(message.getContent())) {
                    message.setIsRisk(1);
                } else {
                    message.setIsRisk(0);
                }
            }
            return messageMapper.update(message) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Message getMessageById(Integer id) {
        try {
            Message message = messageMapper.selectById(id);
            if (message != null) {
                // 查询关联的回复
                Reply reply = replyMapper.selectByMessageId(id);
                message.setReply(reply);
            }
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Message> getMessageList(Integer pageNum, Integer pageSize) {
        try {
            int offset = (pageNum - 1) * pageSize;
            return messageMapper.selectList(offset, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Message> getMessagesByStatus(Integer status, Integer pageNum, Integer pageSize) {
        try {
            int offset = (pageNum - 1) * pageSize;
            return messageMapper.selectByStatus(status, offset, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public int getTotalCount() {
        try {
            return messageMapper.selectCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public int getCountByStatus(Integer status) {
        try {
            return messageMapper.selectCountByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public Map<String, Object> getStatistics() {
        try {
            return messageMapper.selectStatistics();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    @Transactional
    public boolean updateStatus(Integer id, Integer status) {
        try {
            return messageMapper.updateStatus(id, status) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    @Transactional
    public String classifyMessage(Integer id) {
        try {
            Message message = messageMapper.selectById(id);
            if (message != null) {
                String type = tongyiAIUtil.classifyContent(message.getContent());
                messageMapper.updateType(id, type);
                return type;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean checkRiskContent(String content) {
        return tongyiAIUtil.checkRiskContent(content);
    }
    
    @Override
    @Transactional
    public int batchCheckRisk(List<Integer> ids) {
        int count = 0;
        try {
            for (Integer id : ids) {
                Message message = messageMapper.selectById(id);
                if (message != null) {
                    boolean isRisk = tongyiAIUtil.checkRiskContent(message.getContent());
                    messageMapper.updateIsRisk(id, isRisk ? 1 : 0);
                    if (isRisk) {
                        count++;
                    }
                }
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return count;
        }
    }
    
    @Override
    public String generateReply(Integer messageId) {
        try {
            Message message = messageMapper.selectById(messageId);
            if (message != null) {
                return tongyiAIUtil.generateReply(message.getContent(), message.getType());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String polishContent(String content) {
        return tongyiAIUtil.polishContent(content);
    }
}
