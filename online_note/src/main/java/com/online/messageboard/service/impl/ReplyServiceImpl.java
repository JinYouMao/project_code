package com.online.messageboard.service.impl;

import com.online.messageboard.entity.Reply;
import com.online.messageboard.mapper.ReplyMapper;
import com.online.messageboard.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 回复业务实现类
 * 实现回复相关的业务逻辑
 */
@Service
public class ReplyServiceImpl implements ReplyService {
    
    @Autowired
    private ReplyMapper replyMapper;
    
    @Override
    @Transactional
    public boolean addReply(Reply reply) {
        try {
            if (reply.getReplyTime() == null) {
                reply.setReplyTime(new Date());
            }
            return replyMapper.insert(reply) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean deleteReply(Integer id) {
        try {
            return replyMapper.deleteById(id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean updateReply(Reply reply) {
        try {
            if (reply.getReplyTime() == null) {
                reply.setReplyTime(new Date());
            }
            return replyMapper.update(reply) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Reply getReplyById(Integer id) {
        try {
            return replyMapper.selectById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Reply getReplyByMessageId(Integer messageId) {
        try {
            return replyMapper.selectByMessageId(messageId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Reply> getAllReplies() {
        try {
            return replyMapper.selectAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
