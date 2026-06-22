package com.online.messageboard.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 留言实体类
 * 对应数据库中的message表
 */
public class Message implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 留言唯一ID
     */
    private Integer id;
    
    /**
     * 留言人姓名（默认"匿名用户"）
     */
    private String username;
    
    /**
     * 留言正文内容
     */
    private String content;
    
    /**
     * 留言人联系方式
     */
    private String contact;
    
    /**
     * 留言提交时间
     */
    private Date createTime;
    
    /**
     * 留言状态：0=未处理，1=已回复
     */
    private Integer status;
    
    /**
     * AI自动分类：咨询/建议/投诉/其他
     */
    private String type;
    
    /**
     * AI违规标记：0=正常，1=违规
     */
    private Integer isRisk;
    
    /**
     * 关联的回复对象（用于详情页展示）
     */
    private Reply reply;

    public Message() {
    }

    public Message(Integer id, String username, String content, String contact, Date createTime, 
                   Integer status, String type, Integer isRisk) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.contact = contact;
        this.createTime = createTime;
        this.status = status;
        this.type = type;
        this.isRisk = isRisk;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIsRisk() {
        return isRisk;
    }

    public void setIsRisk(Integer isRisk) {
        this.isRisk = isRisk;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", contact='" + contact + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", isRisk=" + isRisk +
                '}';
    }
}
