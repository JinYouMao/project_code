package com.online.messageboard.service.impl;

import com.online.messageboard.entity.Reply;
import com.online.messageboard.mapper.ReplyMapper;
import com.online.messageboard.service.ReplyService;
import com.online.messageboard.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 回复 Service 实现类
 */
@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyMapper replyMapper;

    @Override
    public List<Reply> getByMessageId(Integer messageId) {
        return replyMapper.selectByMessageId(messageId);
    }

    @Override
    public Reply getById(Integer id) {
        return replyMapper.selectById(id);
    }

    @Override
    public boolean add(Reply reply) {
        return replyMapper.insert(reply) > 0;
    }

    @Override
    public boolean update(Integer id, String replyContent) {
        return replyMapper.updateById(id, replyContent, DateUtil.now()) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return replyMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deleteByMessageId(Integer messageId) {
        return replyMapper.deleteByMessageId(messageId) > 0;
    }
}
