package com.online.messageboard.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.online.messageboard.config.TongyiAIConfig;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class TongyiAIUtil {

    private static final Logger logger = LoggerFactory.getLogger(TongyiAIUtil.class);

    @Autowired
    private TongyiAIConfig aiConfig;

    private static TongyiAIUtil instance;

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build();

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

    @PostConstruct
    public void init() { instance = this; }

    public static TongyiAIUtil getInstance() { return instance; }

    private boolean isApiEnabled() {
        if (aiConfig == null) {
            logger.warn("aiConfig is null, using local AI fallback");
            return false;
        }
        return aiConfig.isEnable() && aiConfig.getApiKey() != null && !aiConfig.getApiKey().trim().isEmpty();
    }

    private String callTongyiAPI(String systemPrompt, String userContent) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", aiConfig.getModel());
            JSONObject input = new JSONObject();
            JSONArray messages = new JSONArray();
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemPrompt);
            messages.add(systemMessage);
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", userContent);
            messages.add(userMessage);
            input.put("messages", messages);
            requestBody.put("input", input);
            JSONObject parameters = new JSONObject();
            parameters.put("max_tokens", 2000);
            parameters.put("temperature", 0.7);
            parameters.put("top_p", 0.9);
            requestBody.put("parameters", parameters);

            Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + aiConfig.getApiKey())
                .post(RequestBody.create(requestBody.toJSONString(), JSON_TYPE))
                .build();

            try (Response response = HTTP_CLIENT.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = JSONObject.parseObject(responseBody);
                    JSONObject output = jsonResponse.getJSONObject("output");
                    if (output != null) {
                        JSONArray choices = output.getJSONArray("choices");
                        if (choices != null && choices.size() > 0) {
                            JSONObject choice = choices.getJSONObject(0);
                            JSONObject message = choice.getJSONObject("message");
                            if (message != null) {
                                String result = message.getString("content");
                                logger.info("通义千问API调用成功");
                                return result;
                            }
                        }
                    }
                    logger.error("API响应格式错误: " + responseBody);
                } else {
                    logger.error("API调用失败: " + response.code() + " - " + response.message());
                }
            }
            return null;
        } catch (Exception e) {
            logger.error("通义千问API调用异常", e);
            return null;
        }
    }

    public String polishContent(String content) {
        if (!isApiEnabled()) {
            return AIUtil.polishContent(content);
        }
        String systemPrompt = "你是一个专业的文案润色助手，请帮我润色以下内容，使其语句通顺、表达清晰、礼貌得体，但保持原意不变。直接返回润色后的内容，不要解释。";
        String result = callTongyiAPI(systemPrompt, content);
        return result != null ? result : AIUtil.polishContent(content);
    }

    public String classifyContent(String content) {
        if (!isApiEnabled()) {
            return AIUtil.classifyContent(content);
        }
        String systemPrompt = "请将以下留言内容分类为：咨询、建议、投诉、其他 四种之一。直接返回分类结果，不要解释。";
        String result = callTongyiAPI(systemPrompt, content);
        if (result != null) {
            if (result.contains("咨询")) return "咨询";
            if (result.contains("建议")) return "建议";
            if (result.contains("投诉")) return "投诉";
            return "其他";
        }
        return AIUtil.classifyContent(content);
    }

    public boolean checkRiskContent(String content) {
        if (!isApiEnabled()) {
            return AIUtil.checkRiskContent(content);
        }
        String systemPrompt = "请判断以下内容是否包含违规、敏感、辱骂、广告等不当内容。如果违规，请直接回复\"是\"；如果正常，请直接回复\"否\"。";
        String result = callTongyiAPI(systemPrompt, content);
        if (result != null) {
            return "是".equals(result.trim());
        }
        return AIUtil.checkRiskContent(content);
    }

    public String generateReply(String content, String type) {
        if (!isApiEnabled()) {
            return AIUtil.generateReply(content, type);
        }
        String systemPrompt = "你是一个专业的客服助手，请根据用户的留言内容，生成一条礼貌、专业、详细的回复。回复字数在100-300字之间。";
        String result = callTongyiAPI(systemPrompt, "用户留言类型：" + type + "\n用户留言内容：" + content);
        return result != null ? result : AIUtil.generateReply(content, type);
    }
}
