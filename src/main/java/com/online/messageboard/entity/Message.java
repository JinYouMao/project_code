package com.online.messageboard.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 留言实体类
 * 对应数据库表 message
 */
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 留言唯一ID，主键、自增
     */
    private Integer id;
    /**
     * 留言人姓名，非空，默认值：匿名用户
     */
    private String username;
    /**
     * 留言正文内容，非空
     */
    private String content;
    /**
     * 留言人联系方式，可空
     */
    private String contact;
    /**
     * 留言提交时间，非空
     */
    private Date createTime;
    /**
     * 留言状态：0=未处理，1=已回复
     */
    private Integer status;
    /**
     * AI自动分类：咨询/建议/投诉/感谢/其他
     */
    private String type;
    /**
     * AI违规标记：0=内容正常，1=内容违规
     */
    private Integer isRisk;
    /**
     * 关联的回复对象
     */
    private Reply reply;

    /**
     * 获取状态文本描述
     */
    public String getStatusText() {
        if (status == null) {
            return "未处理";
        }
        return status == 1 ? "已回复" : "未处理";
    }

    /**
     * 获取内容摘要（前50个字符）
     */
    public String getContentSummary() {
        if (content == null || content.isEmpty()) {
            return "";
        }
        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
    }
}
