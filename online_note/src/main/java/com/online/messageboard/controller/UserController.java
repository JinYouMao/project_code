package com.online.messageboard.controller;

import com.online.messageboard.entity.Message;
import com.online.messageboard.service.MessageService;
import com.online.messageboard.util.DateUtil;
import com.online.messageboard.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 用户端控制器
 * 处理用户端的留言相关请求
 */
@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private MessageService messageService;
    
    /**
     * 根路径重定向到用户首页
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/user/index";
    }

    /**
     * 跳转到首页
     */
    @GetMapping("/index")
    public String index() {
        return "user/index";
    }
    
    /**
     * 跳转到留言提交页
     */
    @GetMapping("/add")
    public String add() {
        return "user/add";
    }
    
    /**
     * 跳转到留言详情页
     */
    @GetMapping("/detail")
    public String detail(@RequestParam Integer id, HttpServletRequest request) {
        Message message = messageService.getMessageById(id);
        request.setAttribute("message", message);
        return "user/detail";
    }
    
    /**
     * 分页获取留言列表
     */
    @GetMapping("/list")
    @ResponseBody
    public void listMessages(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletResponse response) {
        
        List<Message> messages = messageService.getMessageList(pageNum, pageSize);
        int total = messageService.getTotalCount();

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
    }
    
    /**
     * 提交留言
     */
    @PostMapping("/submit")
    @ResponseBody
    public void submitMessage(
            @RequestParam(required = false, defaultValue = "") String username,
            @RequestParam String content,
            @RequestParam(required = false, defaultValue = "") String contact,
            HttpServletResponse response) {
        
        try {
            // 参数校验
            if (content == null || content.trim().isEmpty()) {
                WebUtil.writeJSON(response, WebUtil.error("留言内容不能为空"));
                return;
            }
            
            if (content.trim().length() > 2000) {
                WebUtil.writeJSON(response, WebUtil.error("留言内容不能超过2000字"));
                return;
            }
            
            // 创建留言对象
            Message message = new Message();
            message.setUsername(username.trim());
            message.setContent(content.trim());
            message.setContact(contact.trim());
            message.setCreateTime(new Date());
            message.setStatus(0);
            message.setIsRisk(0);
            
            boolean success = messageService.addMessage(message);
            
            if (success) {
                Map<String, Object> data = new HashMap<>();
                data.put("message", "留言提交成功！");
                data.put("id", message.getId());
                WebUtil.writeJSON(response, WebUtil.success("留言提交成功", data));
            } else {
                WebUtil.writeJSON(response, WebUtil.error("留言提交失败，请稍后重试"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("系统错误，请稍后重试"));
        }
    }
    
    /**
     * 获取留言详情
     */
    @GetMapping("/getMessage")
    @ResponseBody
    public void getMessage(@RequestParam Integer id, HttpServletResponse response) {
        try {
            Message message = messageService.getMessageById(id);
            if (message != null) {
                // 格式化日期
                Map<String, Object> data = new HashMap<>();
                data.put("id", message.getId());
                data.put("username", message.getUsername());
                data.put("content", message.getContent());
                data.put("contact", message.getContact());
                data.put("createTime", DateUtil.formatDateTime(message.getCreateTime()));
                data.put("status", message.getStatus());
                data.put("type", message.getType());
                data.put("isRisk", message.getIsRisk());
                
                if (message.getReply() != null) {
                    Map<String, Object> replyData = new HashMap<>();
                    replyData.put("id", message.getReply().getId());
                    replyData.put("replyContent", message.getReply().getReplyContent());
                    replyData.put("replyTime", DateUtil.formatDateTime(message.getReply().getReplyTime()));
                    replyData.put("adminName", message.getReply().getAdminName());
                    data.put("reply", replyData);
                }
                
                WebUtil.writeJSON(response, WebUtil.success("查询成功", data));
            } else {
                WebUtil.writeJSON(response, WebUtil.error("留言不存在"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("系统错误"));
        }
    }
    
    /**
     * AI检测留言内容
     */
    @PostMapping("/checkRisk")
    @ResponseBody
    public void checkRisk(@RequestParam String content, HttpServletResponse response) {
        try {
            boolean isRisk = messageService.checkRiskContent(content);
            Map<String, Object> data = new HashMap<>();
            data.put("isRisk", isRisk);
            data.put("message", isRisk ? "检测到违规内容，请修改后提交" : "内容检测通过");
            WebUtil.writeJSON(response, WebUtil.success("检测完成", data));
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("检测失败"));
        }
    }
    
    /**
     * AI润色留言内容
     */
    @PostMapping("/polish")
    @ResponseBody
    public void polishContent(@RequestParam String content, HttpServletResponse response) {
        try {
            String polished = messageService.polishContent(content);
            Map<String, Object> data = new HashMap<>();
            data.put("content", polished);
            WebUtil.writeJSON(response, WebUtil.success("润色完成", data));
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("润色失败"));
        }
    }
    
    /**
     * 获取AI留言模板
     */
    @GetMapping("/template")
    @ResponseBody
    public void getTemplate(@RequestParam String type, HttpServletResponse response) {
        try {
            String template = com.online.messageboard.util.AIUtil.getTemplate(type);
            Map<String, Object> data = new HashMap<>();
            data.put("template", template);
            WebUtil.writeJSON(response, WebUtil.success("获取成功", data));
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.writeJSON(response, WebUtil.error("获取失败"));
        }
    }
}
