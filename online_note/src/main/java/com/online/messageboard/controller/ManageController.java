package com.online.messageboard.controller;

import com.online.messageboard.entity.Admin;
import com.online.messageboard.entity.Message;
import com.online.messageboard.entity.Reply;
import com.online.messageboard.service.AdminService;
import com.online.messageboard.service.MessageService;
import com.online.messageboard.service.ReplyService;
import com.online.messageboard.util.DateUtil;
import com.online.messageboard.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 管理后台控制器
 * 处理管理后台的所有请求
 */
@Controller
@RequestMapping("/manage")
public class ManageController {
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private ReplyService replyService;
    
    /**
     * 跳转到后台首页
     */
    @GetMapping("/index")
    public String index(HttpServletRequest request) {
        if (!WebUtil.isAdminLoggedIn(request)) {
            return "redirect:/admin/login";
        }
        return "manage/index";
    }
    
    /**
     * 跳转到留言管理页
     */
    @GetMapping("/message")
    public String message(HttpServletRequest request) {
        if (!WebUtil.isAdminLoggedIn(request)) {
            return "redirect:/admin/login";
        }
        return "manage/message";
    }
    
    /**
     * 跳转到回复管理页
     */
    @GetMapping("/reply")
    public String reply(HttpServletRequest request) {
        if (!WebUtil.isAdminLoggedIn(request)) {
            return "redirect:/admin/login";
        }
        return "manage/reply";
    }
    
    /**
     * 跳转到AI统计页
     */
    @GetMapping("/statistics")
    public String statistics(HttpServletRequest request) {
        if (!WebUtil.isAdminLoggedIn(request)) {
            return "redirect:/admin/login";
        }
        return "manage/statistics";
    }
    
    /**
     * 获取留言列表（分页）
     */
    @GetMapping("/message/list")
    @ResponseBody
    public void listMessages(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer status,
            HttpServletRequest request,
            HttpServletResponse response) {
        
        if (!WebUtil.isAdminLoggedIn(request)) {
            WebUtil.writeJSON(response, WebUtil.error("请先登录"));
            return;
        }
        
        try {
            List<Message> messages;
            int total;
            
            if (status != null) {
                messages = messageService.getMessagesByStatus(status, pageNum, pageSize);
                total = messageService.getCountByStatus(status);
            } else {
                messages = messageService.getMessageList(pageNum, pageSize);
                total = messageService.getTotalCount();
            }
            
            // 格式化日期
            List<Map<String, Object>> dataList = new ArrayList<>();
            if (messages != null) {
                for (Message msg : messages) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", msg.getId());
                    item.put("username", msg.getUsername());
                    item.put("content", msg.getContent());
                    item.put("contact", msg.getContact());
                    item.put("createTime", DateUtil.formatDateTime(msg.getCreateTime()));
                    item.put("status", msg.getStatus());
                    item.put("type", msg.getType());
                    item.put("isRisk", msg.getIsRisk());
                    dataList.add(item);
                }
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", dataList);
            result.put("total", total);
            result.put("pageNum", pageNum);
            result.put("pageSize", pageSize);
            
            WebUtil.writeJSON(response, result);
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("获取失败"));
        }
    }
    
    /**
     * 删除留言
     */
    @PostMapping("/message/delete")
    @ResponseBody
    public void deleteMessage(@RequestParam Integer id, HttpServletRequest request, HttpServletResponse response) {
        if (!WebUtil.isAdminLoggedIn(request)) {
            WebUtil.writeJSON(response, WebUtil.error("请先登录"));
            return;
        }
        
        try {
            boolean success = messageService.deleteMessage(id);
            if (success) {
                WebUtil.writeJSON(response, WebUtil.success("删除成功"));
            } else {
                WebUtil.writeJSON(response, WebUtil.error("删除失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("系统错误"));
        }
    }
    
    /**
     * 批量删除留言
     */
    @PostMapping("/message/deleteBatch")
    @ResponseBody
    public void deleteBatch(@RequestParam String ids, HttpServletRequest request, HttpServletResponse response) {
        if (!WebUtil.isAdminLoggedIn(request)) {
            WebUtil.writeJSON(response, WebUtil.error("请先登录"));
            return;
        }
        
        try {
            String[] idArray = ids.split(",");
            List<Integer> idList = new ArrayList<>();
            for (String idStr : idArray) {
                idList.add(Integer.parseInt(idStr.trim()));
            }
            
            boolean success = messageService.deleteBatch(idList);
            if (success) {
                WebUtil.writeJSON(response, WebUtil.success("批量删除成功"));
            } else {
                WebUtil.writeJSON(response, WebUtil.error("批量删除失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("系统错误"));
        }
    }
    
    /**
     * 更新留言状态
     */
    @PostMapping("/message/updateStatus")
    @ResponseBody
    public void updateStatus(
            @RequestParam Integer id,
            @RequestParam Integer status,
            HttpServletRequest request,
            HttpServletResponse response) {
        
        if (!WebUtil.isAdminLoggedIn(request)) {
            WebUtil.writeJSON(response, WebUtil.error("请先登录"));
            return;
        }
        
        try {
            boolean success = messageService.updateStatus(id, status);
            if (success) {
                WebUtil.writeJSON(response, WebUtil.success("状态更新成功"));
            } else {
                WebUtil.writeJSON(response, WebUtil.error("状态更新失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("系统错误"));
        }
    }
    
    /**
     * 添加回复
     */
    @PostMapping("/reply/add")
    @ResponseBody
    public void addReply(
            @RequestParam Integer messageId,
            @RequestParam String content,
            HttpServletRequest request,
            HttpServletResponse response) {
        
        if (!WebUtil.isAdminLoggedIn(request)) {
            WebUtil.writeJSON(response, WebUtil.error("请先登录"));
            return;
        }
        
        try {
            Admin admin = (Admin) WebUtil.getAdminFromSession(request);
            
            // 检查是否已有回复
            Reply existingReply = replyService.getReplyByMessageId(messageId);
            if (existingReply != null) {
                WebUtil.writeJSON(response, WebUtil.error("该留言已有回复，请使用编辑功能"));
                return;
            }
            
            Reply reply = new Reply();
            reply.setMessageId(messageId);
            reply.setReplyContent(content);
            reply.setReplyTime(new Date());
            reply.setAdminName(admin.getUsername());
            
            boolean success = replyService.addReply(reply);
            
            if (success) {
                // 同时更新留言状态为已回复
                messageService.updateStatus(messageId, 1);
                WebUtil.writeJSON(response, WebUtil.success("回复成功"));
            } else {
                WebUtil.writeJSON(response, WebUtil.error("回复失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("系统错误"));
        }
    }
    
    /**
     * 更新回复
     */
    @PostMapping("/reply/update")
    @ResponseBody
    public void updateReply(
            @RequestParam Integer id,
            @RequestParam String content,
            HttpServletRequest request,
            HttpServletResponse response) {
        
        if (!WebUtil.isAdminLoggedIn(request)) {
            WebUtil.writeJSON(response, WebUtil.error("请先登录"));
            return;
        }
        
        try {
            Admin admin = (Admin) WebUtil.getAdminFromSession(request);
            
            Reply reply = replyService.getReplyById(id);
            if (reply == null) {
                WebUtil.writeJSON(response, WebUtil.error("回复不存在"));
                return;
            }
            
            reply.setReplyContent(content);
            reply.setReplyTime(new Date());
            reply.setAdminName(admin.getUsername());
            
            boolean success = replyService.updateReply(reply);
            
            if (success) {
                WebUtil.writeJSON(response, WebUtil.success("更新成功"));
            } else {
                WebUtil.writeJSON(response, WebUtil.error("更新失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("系统错误"));
        }
    }
    
    /**
     * 删除回复
     */
    @PostMapping("/reply/delete")
    @ResponseBody
    public void deleteReply(@RequestParam Integer id, HttpServletRequest request, HttpServletResponse response) {
        if (!WebUtil.isAdminLoggedIn(request)) {
            WebUtil.writeJSON(response, WebUtil.error("请先登录"));
            return;
        }
        
        try {
            Reply reply = replyService.getReplyById(id);
            if (reply != null) {
                // 同时更新留言状态为未回复
                messageService.updateStatus(reply.getMessageId(), 0);
            }
            
            boolean success = replyService.deleteReply(id);
            
            if (success) {
                WebUtil.writeJSON(response, WebUtil.success("删除成功"));
            } else {
                WebUtil.writeJSON(response, WebUtil.error("删除失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("系统错误"));
        }
    }
    
    /**
     * 获取统计数据
     */
    @GetMapping("/statistics/data")
    @ResponseBody
    public void getStatistics(HttpServletRequest request, HttpServletResponse response) {
        if (!WebUtil.isAdminLoggedIn(request)) {
            WebUtil.writeJSON(response, WebUtil.error("请先登录"));
            return;
        }
        
        try {
            Map<String, Object> statistics = messageService.getStatistics();
            
            if (statistics != null) {
                Map<String, Object> safeStats = new HashMap<>();
                safeStats.put("total", toNumber(statistics.get("total")));
                safeStats.put("unprocessed", toNumber(statistics.get("unprocessed")));
                safeStats.put("processed", toNumber(statistics.get("processed")));
                safeStats.put("consultation", toNumber(statistics.get("consultation")));
                safeStats.put("suggestion", toNumber(statistics.get("suggestion")));
                safeStats.put("complaint", toNumber(statistics.get("complaint")));
                safeStats.put("other", toNumber(statistics.get("other")));
                safeStats.put("risky", toNumber(statistics.get("risky")));
                WebUtil.writeJSON(response, WebUtil.success("获取成功", safeStats));
            } else {
                WebUtil.writeJSON(response, WebUtil.error("获取失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("系统错误"));
        }
    }
    
    /**
     * AI智能分类留言
     */
    @PostMapping("/ai/classify")
    @ResponseBody
    public void classifyMessage(@RequestParam Integer id, HttpServletRequest request, HttpServletResponse response) {
        if (!WebUtil.isAdminLoggedIn(request)) {
            WebUtil.writeJSON(response, WebUtil.error("请先登录"));
            return;
        }
        
        try {
            String type = messageService.classifyMessage(id);
            if (type != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("type", type);
                WebUtil.writeJSON(response, WebUtil.success("分类完成", data));
            } else {
                WebUtil.writeJSON(response, WebUtil.error("分类失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("系统错误"));
        }
    }
    
    /**
     * AI生成回复
     */
    @PostMapping("/ai/generateReply")
    @ResponseBody
    public void generateReply(@RequestParam Integer messageId, HttpServletRequest request, HttpServletResponse response) {
        if (!WebUtil.isAdminLoggedIn(request)) {
            WebUtil.writeJSON(response, WebUtil.error("请先登录"));
            return;
        }
        
        try {
            String replyContent = messageService.generateReply(messageId);
            if (replyContent != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("content", replyContent);
                WebUtil.writeJSON(response, WebUtil.success("生成成功", data));
            } else {
                WebUtil.writeJSON(response, WebUtil.error("生成失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("系统错误"));
        }
    }
    
    /**
     * 批量检测违规内容
     */
    @PostMapping("/ai/batchCheckRisk")
    @ResponseBody
    public void batchCheckRisk(HttpServletRequest request, HttpServletResponse response) {
        if (!WebUtil.isAdminLoggedIn(request)) {
            WebUtil.writeJSON(response, WebUtil.error("请先登录"));
            return;
        }
        
        try {
            int total = messageService.getTotalCount();
            List<Message> messages = messageService.getMessageList(1, total);
            
            List<Integer> ids = new ArrayList<>();
            if (messages != null) {
                for (Message msg : messages) {
                    ids.add(msg.getId());
                }
            }
            
            int riskCount = messageService.batchCheckRisk(ids);
            
            Map<String, Object> data = new HashMap<>();
            data.put("total", total);
            data.put("riskCount", riskCount);
            
            WebUtil.writeJSON(response, WebUtil.success("检测完成，发现" + riskCount + "条违规内容", data));
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("检测失败"));
        }
    }

    private int toNumber(Object value) {
        if (value == null) return 0;
        if (value instanceof Number) return ((Number) value).intValue();
        try { return Integer.parseInt(value.toString()); } catch (Exception e) { return 0; }
    }
}
