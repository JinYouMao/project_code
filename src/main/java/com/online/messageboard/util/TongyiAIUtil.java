package com.online.messageboard.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.online.messageboard.config.TongyiAIConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 通义千问 AI API 工具类
 * 调用通义千问大模型 API
 */
@Slf4j
@Component
public class TongyiAIUtil {

    @Autowired
    private TongyiAIConfig aiConfig;

    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    
    // 直接硬编码API key，避免配置读取问题
    private static final String DIRECT_API_KEY = "sk-a322f7d891b340ada423f4ff76ff3f10";

    private OkHttpClient httpClient;

    public TongyiAIUtil() {
        // 初始化 HTTP 客户端
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @javax.annotation.PostConstruct
    public void init() {
        log.info("[通义千问API]初始化 - 直接使用硬编码的API key");
    }

    /**
     * 调用通义千问 API 获取响应
     */
    public String callTongyiAI(String prompt) throws IOException {
        // 直接使用硬编码的API key
        log.info("[通义千问API]开始调用真实API...使用直接API key");
        
        try {
            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "qwen-turbo");

            JSONObject input = new JSONObject();
            JSONArray messages = new JSONArray();
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);
            messages.add(message);
            input.put("messages", messages);
            requestBody.put("input", input);

            JSONObject parameters = new JSONObject();
            parameters.put("result_format", "message");
            requestBody.put("parameters", parameters);

            // 构建请求
            Request request = new Request.Builder()
                    .url(API_URL)
                    .addHeader("Authorization", "Bearer " + DIRECT_API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(MediaType.parse("application/json"), requestBody.toJSONString()))
                    .build();

            log.info("[通义千问API]正在发送请求...");
            
            // 发送请求
            try (Response response = httpClient.newCall(request).execute()) {
                log.info("[通义千问API]收到响应，HTTP code: {}", response.code());
                
                if (!response.isSuccessful()) {
                    log.error("[通义千问API]请求失败，HTTP code: {}", response.code());
                    String errorBody = response.body() != null ? response.body().string() : "no body";
                    log.error("[通义千问API]错误响应: {}", errorBody);
                    throw new IOException("API request failed, code: " + response.code());
                }

                String responseBody = response.body().string();
                log.info("[通义千问API]响应成功");

                // 解析响应
                JSONObject resultJson = JSON.parseObject(responseBody);
                JSONObject output = resultJson.getJSONObject("output");
                JSONArray choices = output.getJSONArray("choices");
                if (choices != null && choices.size() > 0) {
                    JSONObject choice = choices.getJSONObject(0);
                    JSONObject messageObj = choice.getJSONObject("message");
                    String result = messageObj.getString("content");
                    log.info("[通义千问API]解析结果: {}", result);
                    return result;
                }

                log.warn("[通义千问API]响应中没有choices，返回null");
                return null;
            }
        } catch (Exception e) {
            log.error("[通义千问API]调用异常", e);
            throw e;
        }
    }

    /**
     * 违规检测（带具体违规词）
     */
    public String checkRiskWithWord(String content) {
        try {
            String prompt = "任务：检查以下内容是否包含违规内容（粗口、脏话、色情、暴力、反动、辱骂等）\n" +
                          "要求：\n" +
                          "1. 如果有违规，请找出并返回具体的违规词汇（只返回词汇，多个词汇用逗号分隔）\n" +
                          "2. 如果没有违规，返回：无\n" +
                          "3. 不要解释，不要添加其他内容\n" +
                          "\n内容：\n" + content;
            String aiResponse = callTongyiAI(prompt);
            log.info("[通义千问API]违规检测结果: {}", aiResponse);
            if (aiResponse != null && !aiResponse.contains("无")) {
                return aiResponse;
            }
        } catch (Exception e) {
            log.error("[通义千问API]违规检测异常", e);
        }
        // 如果AI返回null，或者返回"无"，尝试本地检测
        String localRiskWord = AIUtil.findRiskWord(content);
        return localRiskWord;
    }
    
    /**
     * 违规检测
     */
    public boolean checkRisk(String content) {
        try {
            String prompt = "任务：检查以下内容是否包含违规内容（粗口、脏话、色情、暴力、反动、辱骂等）\n" +
                          "要求：\n" +
                          "1. 如果有违规，只回答：是\n" +
                          "2. 如果没有违规，只回答：否\n" +
                          "3. 不要解释，不要添加其他内容\n" +
                          "\n内容：\n" + content;
            String aiResponse = callTongyiAI(prompt);
            log.info("[通义千问API]违规检测结果: {}", aiResponse);
            if (aiResponse != null) {
                return aiResponse.contains("是");
            }
        } catch (Exception e) {
            log.error("[通义千问API]违规检测异常", e);
        }
        return AIUtil.checkRisk(content);
    }

    /**
     * 内容分类
     */
    public String classifyContent(String content) {
        try {
            String prompt = "请将以下内容分类为：咨询/建议/投诉/感谢/其他。只需返回分类结果：\n\n" + content;
            String aiResponse = callTongyiAI(prompt);
            if (aiResponse != null) {
                if (aiResponse.contains("咨询")) return "咨询";
                if (aiResponse.contains("建议")) return "建议";
                if (aiResponse.contains("投诉")) return "投诉";
                if (aiResponse.contains("感谢")) return "感谢";
                return "其他";
            }
        } catch (Exception e) {
            log.error("[通义千问API]内容分类异常", e);
        }
        return AIUtil.classifyContent(content);
    }

    /**
     * 内容润色
     */
    public String polishContent(String content) {
        try {
            String prompt = "任务：润色以下留言内容\n" +
                          "要求：\n" +
                          "1. 使内容更通顺、规范、礼貌\n" +
                          "2. 保持原意，不要改变核心意思\n" +
                          "3. 只返回润色后的内容，不要解释\n" +
                          "4. 不要在开头加【润色后】之类的标注\n" +
                          "5. 如果有脏话、粗口，先去除或替换为礼貌表达\n" +
                          "\n原内容：\n" + content;
            String aiResponse = callTongyiAI(prompt);
            log.info("[通义千问API]润色结果: {}", aiResponse);
            if (aiResponse != null) {
                // 清理AI可能返回的多余内容
                aiResponse = aiResponse.trim();
                if (aiResponse.startsWith("润色后：") || aiResponse.startsWith("润色后:")) {
                    aiResponse = aiResponse.substring(4).trim();
                }
                if (aiResponse.startsWith("【润色后】")) {
                    aiResponse = aiResponse.substring(5).trim();
                }
                return aiResponse;
            }
        } catch (Exception e) {
            log.error("[通义千问API]内容润色异常", e);
        }
        return AIUtil.polishContent(content);
    }

    /**
     * 生成回复
     */
    public String generateReply(String content, String type) {
        try {
            String prompt = "请为以下内容生成一个礼貌、专业的管理员回复（约100字）：\n\n" + content;
            String aiResponse = callTongyiAI(prompt);
            if (aiResponse != null) {
                return aiResponse;
            }
        } catch (Exception e) {
            log.error("[通义千问API]生成回复异常", e);
        }
        return AIUtil.generateReply(content, type);
    }
}
