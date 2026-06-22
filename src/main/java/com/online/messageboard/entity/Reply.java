package com.online.messageboard.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 回复实体类
 * 对应数据库表 reply
 */
@Data
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 回复唯一ID，主键、自增
     */
    private Integer id;
    /**
     * 关联的留言ID，非空，外键关联 message.id
     */
    private Integer messageId;
    /**
     * 管理员回复内容，非空
     */
    private String replyContent;
    /**
     * 回复提交时间，非空
     */
    private Date replyTime;
    /**
     * 操作的管理员账号，非空
     */
    private String adminName;
}
