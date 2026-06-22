package com.online.messageboard.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Web工具类
 * 提供Web层常用的工具方法
 */
public class WebUtil {
    
    // Session中管理员信息的键名
    public static final String ADMIN_SESSION_KEY = "admin";
    
    /**
     * 判断请求是否为Ajax请求
     * @param request HTTP请求
     * @return 是否为Ajax请求
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(header);
    }
    
    /**
     * 获取Session中的管理员信息
     * @param request HTTP请求
     * @return 管理员对象，未登录返回null
     */
    public static Object getAdminFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getAttribute(ADMIN_SESSION_KEY);
        }
        return null;
    }
    
    /**
     * 设置Session中的管理员信息
     * @param request HTTP请求
     * @param admin 管理员对象
     */
    public static void setAdminToSession(HttpServletRequest request, Object admin) {
        HttpSession session = request.getSession(true);
        session.setAttribute(ADMIN_SESSION_KEY, admin);
    }
    
    /**
     * 移除Session中的管理员信息
     * @param request HTTP请求
     */
    public static void removeAdminFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(ADMIN_SESSION_KEY);
        }
    }
    
    /**
     * 检查管理员是否已登录
     * @param request HTTP请求
     * @return 是否已登录
     */
    public static boolean isAdminLoggedIn(HttpServletRequest request) {
        return getAdminFromSession(request) != null;
    }
    
    /**
     * 返回JSON响应
     * @param response HTTP响应
     * @param data 返回数据
     */
    public static void writeJSON(HttpServletResponse response, Object data) {
        writeJSON(response, 200, data);
    }
    
    /**
     * 返回JSON响应
     * @param response HTTP响应
     * @param status HTTP状态码
     * @param data 返回数据
     */
    public static void writeJSON(HttpServletResponse response, int status, Object data) {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        try {
            PrintWriter out = response.getWriter();
            out.print(toJSON(data));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 将对象转换为JSON字符串（简化版）
     * @param obj 对象
     * @return JSON字符串
     */
    private static String toJSON(Object obj) {
        if (obj == null) {
            return "null";
        }
        
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) {
                    sb.append(",");
                }
                sb.append("\"").append(entry.getKey()).append("\":");
                sb.append(toJSON(entry.getValue()));
                first = false;
            }
            sb.append("}");
            return sb.toString();
        }
        
        if (obj instanceof Iterable) {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : (Iterable<?>) obj) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(toJSON(item));
                first = false;
            }
            sb.append("]");
            return sb.toString();
        }
        
        if (obj instanceof String) {
            return "\"" + escapeJSON((String) obj) + "\"";
        }
        
        if (obj instanceof Number) {
            return obj.toString();
        }
        
        if (obj instanceof Boolean) {
            return obj.toString();
        }
        
        if (obj instanceof java.math.BigDecimal) {
            return obj.toString();
        }

        return "\"" + escapeJSON(obj.toString()) + "\"";
    }
    
    /**
     * 转义JSON字符串
     * @param str 字符串
     * @return 转义后的字符串
     */
    private static String escapeJSON(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    /**
     * 获取客户端真实IP地址
     * @param request HTTP请求
     * @return IP地址
     */
    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**
     * 返回成功响应
     * @param message 消息
     * @return 响应Map
     */
    public static Map<String, Object> success(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", message);
        return result;
    }
    
    /**
     * 返回失败响应
     * @param message 消息
     * @return 响应Map
     */
    public static Map<String, Object> error(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        return result;
    }
    
    /**
     * 返回成功响应（带数据）
     * @param message 消息
     * @param data 数据
     * @return 响应Map
     */
    public static Map<String, Object> success(String message, Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", message);
        result.put("data", data);
        return result;
    }
}
