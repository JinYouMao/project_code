package com.online.messageboard.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * 提供统一的日期格式化和解析方法
 */
public class DateUtil {
    
    // 标准日期格式：yyyy-MM-dd HH:mm:ss
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    // 日期格式：yyyy-MM-dd
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    // 时间格式：HH:mm:ss
    public static final String TIME_FORMAT = "HH:mm:ss";
    
    /**
     * 格式化日期为标准字符串
     * @param date 日期对象
     * @return 格式化后的字符串（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        return sdf.format(date);
    }
    
    /**
     * 格式化日期
     * @param date 日期对象
     * @param pattern 格式模式
     * @return 格式化后的字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    /**
     * 解析日期字符串
     * @param dateStr 日期字符串
     * @return Date对象
     */
    public static Date parse(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 解析日期字符串
     * @param dateStr 日期字符串
     * @param pattern 格式模式
     * @return Date对象
     */
    public static Date parse(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取当前日期时间字符串
     * @return 当前日期时间（yyyy-MM-dd HH:mm:ss）
     */
    public static String getNowDateTimeStr() {
        return formatDateTime(new Date());
    }
    
    /**
     * 获取当前日期字符串
     * @return 当前日期（yyyy-MM-dd）
     */
    public static String getNowDateStr() {
        return format(new Date(), DATE_FORMAT);
    }
    
    /**
     * 计算两个日期之间的天数差
     * @param start 开始日期
     * @param end 结束日期
     * @return 天数差
     */
    public static long getDaysBetween(Date start, Date end) {
        if (start == null || end == null) {
            return 0;
        }
        long diff = end.getTime() - start.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }
    
    /**
     * 格式化相对时间（如：刚刚、5分钟前、1小时前等）
     * @param date 日期对象
     * @return 相对时间字符串
     */
    public static String formatRelativeTime(Date date) {
        if (date == null) {
            return "";
        }
        
        long diff = System.currentTimeMillis() - date.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (seconds < 60) {
            return "刚刚";
        } else if (minutes < 60) {
            return minutes + "分钟前";
        } else if (hours < 24) {
            return hours + "小时前";
        } else if (days < 7) {
            return days + "天前";
        } else {
            return formatDateTime(date);
        }
    }
}
