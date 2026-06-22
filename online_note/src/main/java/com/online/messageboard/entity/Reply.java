package com.online.messageboard.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 回复实体类
 * 对应数据库中的reply表
 */
public class Reply implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 回复唯一ID
     */
    private Integer id;
    
    /**
     * 关联的留言ID
     */
    private Integer messageId;
    
    /**
     * 管理员回复内容
     */
    private String replyContent;
    
    /**
     * 回复提交时间
     */
    private Date replyTime;
    
    /**
     * 操作的管理员账号
     */
    private String adminName;

    public Reply() {
    }

    public Reply(Integer id, Integer messageId, String replyContent, Date replyTime, String adminName) {
        this.id = id;
        this.messageId = messageId;
        this.replyContent = replyContent;
        this.replyTime = replyTime;
        this.adminName = adminName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", messageId=" + messageId +
                ", replyContent='" + replyContent + '\'' +
                ", replyTime=" + replyTime +
                ", adminName='" + adminName + '\'' +
                '}';
    }
}
