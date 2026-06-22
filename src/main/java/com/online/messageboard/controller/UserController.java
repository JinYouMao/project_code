package com.online.messageboard.controller;

import com.online.messageboard.entity.Message;
import com.online.messageboard.entity.User;
import com.online.messageboard.service.MessageService;
import com.online.messageboard.service.UserService;
import com.online.messageboard.util.AIUtil;
import com.online.messageboard.util.DateUtil;
import com.online.messageboard.util.TongyiAIUtil;
import com.online.messageboard.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 用户端 Controller
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private TongyiAIUtil aiUtil;

    /**
     * 检查用户是否登录
     */
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    /**
     * 首页 - 留言列表
     */
    @GetMapping({"/", "/index"})
    public String index(@RequestParam(defaultValue = "1") int pageNum,
                        @RequestParam(defaultValue = "10") int pageSize,
                        Model model,
                        HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/user/login";
        }
        List<Message> messages = messageService.getByPage(pageNum, pageSize);
        int totalCount = messageService.countAll();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        boolean hasPrev = pageNum > 1;
        boolean hasNext = pageNum < totalPages;

        model.addAttribute("messages", messages);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("hasNext", hasNext);

        return "user/index";
    }

    /**
     * 留言详情
     */
    @GetMapping("/detail")
    public String detail(@RequestParam Integer id, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/user/login";
        }
        Message message = messageService.getById(id);
        model.addAttribute("message", message);
        return "user/detail";
    }

    /**
     * 发表留言页面
     */
    @GetMapping("/add")
    public String addPage(HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/user/login";
        }
        return "user/add";
    }

    /**
     * 提交留言 (原AJAX方式 - 用于前端验证违规检测
     */
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addMessage(@RequestParam(required = false) String username,
                                          @RequestParam String content,
                                          @RequestParam(required = false) String contact,
                                          HttpSession session) {
        if (!isLoggedIn(session)) {
            return WebUtil.error("请先登录");
        }
        try {
            Message message = new Message();
            message.setUsername(username != null && !username.isEmpty() ? username : "匿名用户");
            message.setContent(content);
            message.setContact(contact);
            message.setCreateTime(DateUtil.now());
            message.setStatus(0);

            // AI 分类和违规检测
            String type = aiUtil.classifyContent(content);
            boolean isRisk = aiUtil.checkRisk(content);
            message.setType(type);
            message.setIsRisk(isRisk ? 1 : 0);

            boolean success = messageService.add(message);
            if (success) {
                return WebUtil.success();
            } else {
                return WebUtil.error("提交失败");
            }
        } catch (Exception e) {
            return WebUtil.error(e.getMessage());
        }
    }
    
    /**
     * 提交留言 (直接表单方式) - 用于直接提交后跳转
     */
    @PostMapping("/add-direct")
    public String addMessageDirect(@RequestParam(required = false) String username,
                                  @RequestParam String content,
                                  @RequestParam(required = false) String contact,
                                  HttpSession session) {
        log.info("[用户提交] 开始处理 - username: {}, content: {}, contact: {}", 
                 username, content, contact);
        if (!isLoggedIn(session)) {
            log.info("[用户提交] 未登录，跳转到登录页");
            return "redirect:/user/login";
        }
        try {
            Message message = new Message();
            message.setUsername(username != null && !username.isEmpty() ? username : "匿名用户");
            message.setContent(content);
            message.setContact(contact);
            message.setCreateTime(DateUtil.now());
            message.setStatus(0);

            // AI 分类和违规检测
            String type = aiUtil.classifyContent(content);
            boolean isRisk = aiUtil.checkRisk(content);
            message.setType(type);
            message.setIsRisk(isRisk ? 1 : 0);

            log.info("[用户提交] 保存留言到数据库 - type: {}, isRisk: {}", type, isRisk);
            messageService.add(message);
            log.info("[用户提交] 保存成功，跳转到首页");
            // 直接跳转到首页
            return "redirect:/user/index";
        } catch (Exception e) {
            log.error("[用户提交] 异常", e);
            return "redirect:/user/add?error=1";
        }
    }

    /**
     * AI 内容润色
     */
    @PostMapping("/ai/polish")
    @ResponseBody
    public Map<String, Object> aiPolish(@RequestParam String content) {
        try {
            String polished = aiUtil.polishContent(content);
            Map<String, Object> result = WebUtil.success();
            result.put("data", polished);
            return result;
        } catch (Exception e) {
            return WebUtil.error(e.getMessage());
        }
    }

    /**
     * AI 违规检测
     */
    @PostMapping("/ai/checkRisk")
    @ResponseBody
    public Map<String, Object> aiCheckRisk(@RequestParam String content) {
        try {
            // 使用新的带违规词检测
            String riskWord = aiUtil.checkRiskWithWord(content);
            boolean isRisk = riskWord != null && !"无".equals(riskWord) && !"".equals(riskWord);
            
            Map<String, Object> result = WebUtil.success();
            result.put("data", isRisk);
            
            if (isRisk) {
                result.put("riskWord", riskWord);
            }
            
            return result;
        } catch (Exception e) {
            return WebUtil.error(e.getMessage());
        }
    }

    /**
     * 获取模板
     */
    @GetMapping("/ai/template")
    @ResponseBody
    public Map<String, Object> getTemplate(@RequestParam String type) {
        try {
            String template = AIUtil.getTemplate(type);
            Map<String, Object> result = WebUtil.success();
            result.put("data", template);
            return result;
        } catch (Exception e) {
            return WebUtil.error(e.getMessage());
        }
    }

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    /**
     * 登录处理
     */
    @PostMapping("/doLogin")
    @ResponseBody
    public Map<String, Object> doLogin(@RequestParam String username,
                                       @RequestParam String password,
                                       HttpSession session) {
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return WebUtil.success();
        } else {
            return WebUtil.error("用户名或密码错误");
        }
    }

    /**
     * 注册页面
     */
    @GetMapping("/register")
    public String registerPage() {
        return "user/register";
    }

    /**
     * 注册处理
     */
    @PostMapping("/doRegister")
    @ResponseBody
    public Map<String, Object> doRegister(@RequestParam String username,
                                          @RequestParam String password,
                                          @RequestParam(required = false) String nickname,
                                          @RequestParam(required = false) String email,
                                          @RequestParam(required = false) String phone) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setCreateTime(DateUtil.now());

        boolean success = userService.register(user);
        if (success) {
            return WebUtil.success();
        } else {
            return WebUtil.error("用户名已存在");
        }
    }

    /**
     * 登出
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
}
