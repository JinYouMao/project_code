package com.online.messageboard.controller;

import com.online.messageboard.entity.Admin;
import com.online.messageboard.entity.Message;
import com.online.messageboard.entity.Reply;
import com.online.messageboard.service.MessageService;
import com.online.messageboard.service.ReplyService;
import com.online.messageboard.util.DateUtil;
import com.online.messageboard.util.TongyiAIUtil;
import com.online.messageboard.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 后台管理 Controller
 */
@Controller
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private TongyiAIUtil aiUtil;

    /**
     * 管理首页
     */
    @GetMapping("/index")
    public String index(Model model) {
        int totalCount = messageService.countAll();
        int unhandledCount = messageService.countByStatus(0);
        int repliedCount = messageService.countByStatus(1);
        int riskCount = messageService.getRiskCount();

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("unhandledCount", unhandledCount);
        model.addAttribute("repliedCount", repliedCount);
        model.addAttribute("riskCount", riskCount);

        return "manage/index";
    }

    /**
     * 留言列表
     */
    @GetMapping("/message")
    public String messageList(@RequestParam(required = false) Integer status,
                              @RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize,
                              Model model) {
        List<Message> messages;
        int totalCount;

        if (status != null) {
            messages = messageService.getByStatusAndPage(status, pageNum, pageSize);
            totalCount = messageService.countByStatus(status);
        } else {
            messages = messageService.getByPage(pageNum, pageSize);
            totalCount = messageService.countAll();
        }

        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        boolean hasPrev = pageNum > 1;
        boolean hasNext = pageNum < totalPages;

        model.addAttribute("messages", messages);
        model.addAttribute("status", status);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("hasNext", hasNext);

        return "manage/message";
    }

    /**
     * 回复管理页面
     */
    @GetMapping("/reply")
    public String replyPage(@RequestParam(required = false) Integer messageId, Model model) {
        // 如果没有messageId，重定向回留言列表
        if (messageId == null) {
            return "redirect:/manage/message";
        }
        
        Message message = messageService.getById(messageId);
        if (message == null) {
            return "redirect:/manage/message";
        }
        
        List<Reply> replies = replyService.getByMessageId(messageId);

        model.addAttribute("message", message);
        model.addAttribute("replies", replies);

        return "manage/reply";
    }

    /**
     * 添加回复
     */
    @PostMapping("/reply/add")
    @ResponseBody
    public Map<String, Object> addReply(@RequestParam Integer messageId,
                                        @RequestParam String replyContent,
                                        HttpSession session) {
        try {
            Admin admin = (Admin) session.getAttribute("admin");

            Reply reply = new Reply();
            reply.setMessageId(messageId);
            reply.setReplyContent(replyContent);
            reply.setReplyTime(DateUtil.now());
            reply.setAdminName(admin.getUsername());

            boolean success = replyService.add(reply);
            if (success) {
                messageService.updateStatus(messageId, 1);
                return WebUtil.success();
            } else {
                return WebUtil.error("添加失败");
            }
        } catch (Exception e) {
            return WebUtil.error(e.getMessage());
        }
    }

    /**
     * 更新回复
     */
    @PostMapping("/reply/update")
    @ResponseBody
    public Map<String, Object> updateReply(@RequestParam Integer id,
                                           @RequestParam String replyContent) {
        try {
            boolean success = replyService.update(id, replyContent);
            if (success) {
                return WebUtil.success();
            } else {
                return WebUtil.error("更新失败");
            }
        } catch (Exception e) {
            return WebUtil.error(e.getMessage());
        }
    }
    
    /**
     * 删除回复
     */
    @PostMapping("/reply/delete")
    @ResponseBody
    public Map<String, Object> deleteReply(@RequestParam Integer id,
                                           @RequestParam Integer messageId) {
        try {
            boolean success = replyService.delete(id);
            if (success) {
                List<Reply> replies = replyService.getByMessageId(messageId);
                if (replies.isEmpty()) {
                    messageService.updateStatus(messageId, 0);
                }
                return WebUtil.success();
            } else {
                return WebUtil.error("删除失败");
            }
        } catch (Exception e) {
            return WebUtil.error(e.getMessage());
        }
    }

    /**
     * 更新留言状态
     */
    @PostMapping("/message/updateStatus")
    @ResponseBody
    public Map<String, Object> updateStatus(@RequestParam Integer id,
                                            @RequestParam Integer status) {
        try {
            boolean success = messageService.updateStatus(id, status);
            if (success) {
                return WebUtil.success();
            } else {
                return WebUtil.error("更新失败");
            }
        } catch (Exception e) {
            return WebUtil.error(e.getMessage());
        }
    }

    /**
     * 删除留言
     */
    @PostMapping("/message/delete")
    @ResponseBody
    public Map<String, Object> deleteMessage(@RequestParam Integer id) {
        try {
            boolean success = messageService.delete(id);
            if (success) {
                return WebUtil.success();
            } else {
                return WebUtil.error("删除失败");
            }
        } catch (Exception e) {
            return WebUtil.error(e.getMessage());
        }
    }

    /**
     * 批量删除留言
     */
    @PostMapping("/message/deleteBatch")
    @ResponseBody
    public Map<String, Object> deleteBatch(@RequestParam String idsStr) {
        try {
            List<Integer> ids = Arrays.stream(idsStr.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            boolean success = messageService.deleteBatch(ids);
            if (success) {
                return WebUtil.success();
            } else {
                return WebUtil.error("删除失败");
            }
        } catch (Exception e) {
            return WebUtil.error(e.getMessage());
        }
    }

    /**
     * AI 分类
     */
    @PostMapping("/ai/classify")
    @ResponseBody
    public Map<String, Object> aiClassify(@RequestParam Integer id) {
        try {
            Message message = messageService.getById(id);
            String type = aiUtil.classifyContent(message.getContent());
            boolean isRisk = aiUtil.checkRisk(message.getContent());

            messageService.updateTypeAndRisk(id, type, isRisk ? 1 : 0);
            Map<String, Object> result = WebUtil.success();
            result.put("type", type);
            result.put("isRisk", isRisk);
            return result;
        } catch (Exception e) {
            return WebUtil.error(e.getMessage());
        }
    }

    /**
     * AI 批量违规检测
     */
    @PostMapping("/ai/batchCheckRisk")
    @ResponseBody
    public Map<String, Object> batchCheckRisk() {
        try {
            List<Message> messages = messageService.getAll();
            int updateCount = 0;
            for (Message message : messages) {
                String type = aiUtil.classifyContent(message.getContent());
                boolean isRisk = aiUtil.checkRisk(message.getContent());
                messageService.updateTypeAndRisk(message.getId(), type, isRisk ? 1 : 0);
                updateCount++;
            }
            Map<String, Object> result = WebUtil.success();
            result.put("count", updateCount);
            return result;
        } catch (Exception e) {
            return WebUtil.error(e.getMessage());
        }
    }

    /**
     * AI 单个违规检查
     */
    @PostMapping("/ai/checkRisk")
    @ResponseBody
    public Map<String, Object> aiCheckRisk(@RequestParam Integer id) {
        try {
            Message message = messageService.getById(id);
            boolean isRisk = aiUtil.checkRisk(message.getContent());
            
            // 更新数据库
            messageService.updateTypeAndRisk(id, message.getType(), isRisk ? 1 : 0);
            
            Map<String, Object> result = WebUtil.success();
            result.put("isRisk", isRisk);
            return result;
        } catch (Exception e) {
            return WebUtil.error(e.getMessage());
        }
    }

    /**
     * AI 生成回复
     */
    @PostMapping("/ai/generateReply")
    @ResponseBody
    public Map<String, Object> aiGenerateReply(@RequestParam Integer id) {
        try {
            Message message = messageService.getById(id);
            String type = message.getType() != null ? message.getType() : "其他";
            String reply = aiUtil.generateReply(message.getContent(), type);
            Map<String, Object> result = WebUtil.success();
            result.put("reply", reply);
            return result;
        } catch (Exception e) {
            return WebUtil.error(e.getMessage());
        }
    }

    /**
     * 数据统计
     */
    @GetMapping("/statistics")
    public String statistics(Model model) {
        int totalCount = messageService.countAll();
        int unhandledCount = messageService.countByStatus(0);
        int repliedCount = messageService.countByStatus(1);
        int riskCount = messageService.getRiskCount();
        List<Map<String, Object>> typeStats = messageService.getTypeStats();

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("unhandledCount", unhandledCount);
        model.addAttribute("repliedCount", repliedCount);
        model.addAttribute("riskCount", riskCount);
        model.addAttribute("typeStats", typeStats);

        return "manage/statistics";
    }
}
