package com.online.messageboard.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 通义千问 AI 配置类
 * 从 application.yml 中读取 ai.tongyi 前缀的配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "ai.tongyi")
public class TongyiAIConfig {

    /**
     * API 密钥
     */
    private String apiKey;
    /**
     * 模型名称，默认 qwen-turbo
     */
    private String model = "qwen-turbo";
    /**
     * 是否启用 AI 功能，默认 true
     */
    private boolean enable = true;
    /**
     * 超时时间，默认30000毫秒
     */
    private int timeout = 30000;
}
