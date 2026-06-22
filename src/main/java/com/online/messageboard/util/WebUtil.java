package com.online.messageboard.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Web 工具类
 */
public class WebUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 写 JSON 响应
     */
    public static void writeJson(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }

    /**
     * 创建成功响应 Map
     */
    public static Map<String, Object> success() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "操作成功");
        return result;
    }

    /**
     * 创建带数据的成功响应 Map
     */
    public static Map<String, Object> success(Object data) {
        Map<String, Object> result = success();
        result.put("data", data);
        return result;
    }

    /**
     * 创建失败响应 Map
     */
    public static Map<String, Object> error(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("message", message);
        return result;
    }
}
