package com.online.messageboard.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * AI 智能助手工具类（本地模拟版本）
 * 提供内容分类、违规检测、内容润色、回复生成等功能
 */
@Slf4j
public class AIUtil {

    /**
     * 违规关键词库（用于本地模拟违规检测
     */
    private static final Set<String> RISK_WORDS = new HashSet<>(Arrays.asList(
            "操", "操你", "靠", "艹", "日", "干", "屌", "逼", "妈逼", "傻逼",
            "暴力", "色情", "赌博", "诈骗", "反动", "敏感", "违法", "违规",
            "攻击", "侮辱", "诽谤", "虚假", "谣言", "去死", "废物", "垃圾",
            "蠢", "笨", "猪", "傻", "白痴", "弱智", "脑残", "贱", "恶心",
            "操蛋", "王八蛋", "龟孙子", "他妈的", "你妈的", "肏", "屁眼",
            "淫秽", "性交易", "嫖娼", "卖淫", "裸聊", "裸照", "黄色",
            "尼玛", "尼玛的", "草泥马", "麻痹", "我靠",
            "特么的", "特么", "尼玛", "尼玛的", "草泥马"
    ));

    /**
     * 各类型关键词（咨询
     */
    private static final String TYPE_CONSULT = "咨询";
    private static final String TYPE_SUGGEST = "建议";
    private static final String TYPE_COMPLAINT = "投诉";
    private static final String TYPE_THANKS = "感谢";
    private static final String TYPE_OTHER = "其他";

    /**
     * 违规检测
     * @param content 待检测的内容
     * @return true=违规，false=正常
     */
    public static boolean checkRisk(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }

        // 转换为小写，简化匹配
        String lowerContent = content.toLowerCase();

        // 检测关键词匹配
        for (String word : RISK_WORDS) {
            if (lowerContent.contains(word.toLowerCase())) {
                log.info("[本地AI违规检测：发现违规关键词：{}", word);
                return true;
            }
        }

        return false;
    }
    
    /**
     * 检测并返回违规词
     * @param content 待检测的内容
     * @return 发现的违规词，如果没有返回null
     */
    public static String findRiskWord(String content) {
        if (content == null || content.trim().isEmpty()) {
            return null;
        }

        String lowerContent = content.toLowerCase();
        for (String word : RISK_WORDS) {
            if (lowerContent.contains(word.toLowerCase())) {
                return word;
            }
        }
        return null;
    }

    /**
     * 内容分类
     * @param content 待分类的内容
     * @return 分类结果：咨询/建议/投诉/感谢/其他
     */
    public static String classifyContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return TYPE_OTHER;
        }

        String lowerContent = content.toLowerCase();

        // 感谢
        if (lowerContent.contains("谢谢") || lowerContent.contains("感谢") || 
            lowerContent.contains("好") || lowerContent.contains("棒")) {
            return TYPE_THANKS;
        }

        // 投诉
        if (lowerContent.contains("投诉") || lowerContent.contains("不满") || 
            lowerContent.contains("问题") || lowerContent.contains("差")) {
            return TYPE_COMPLAINT;
        }

        // 建议
        if (lowerContent.contains("建议") || lowerContent.contains("希望") || 
            lowerContent.contains("应该")) {
            return TYPE_SUGGEST;
        }

        // 咨询
        if (lowerContent.contains("请问") || lowerContent.contains("如何") || 
            lowerContent.contains("怎么") || lowerContent.contains("吗") || 
            lowerContent.contains("？") || lowerContent.contains("问")) {
            return TYPE_CONSULT;
        }

        return TYPE_OTHER;
    }

    /**
     * 内容润色
     * @param content 待润色的内容
     * @return 润色后的内容
     */
    public static String polishContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return content;
        }

        String polished = content;

        // 替换常见口语化表达
        polished = polished.replace("啥", "什么").replace("咋", "怎么");
        polished = polished.replace("酱紫", "这样子");
        polished = polished.replace("咋滴", "怎么样").replace("咋地", "怎么样");
        polished = polished.replace("哦", "好的").replace("嗯", "好的");
        polished = polished.replace("噢", "好的").replace("喔", "好的");
        polished = polished.replace("哈", "").replace("哈哈", "");
        polished = polished.replace("emmm", "嗯");
        polished = polished.replace("emm", "嗯");
        polished = polished.replace("......", "……");
        
        // 修复重复标点
        polished = polished.replace("！！！", "！").replace("！！", "！");
        polished = polished.replace("？？？", "？").replace("？？", "？");
        polished = polished.replace("。。。", "。").replace("。。", "。");
        
        // 智能润色：自动添加礼貌用语（但不要重复添加）
        if (!polished.startsWith("您好") && !polished.startsWith("您好！") && 
            !polished.startsWith("你好") && !polished.startsWith("你好！") &&
            !polished.startsWith("尊敬") && !polished.startsWith("您好,")) {
            // 如果是咨询类，加礼貌的开头
            if (polished.contains("请问") || polished.contains("问") || 
                polished.contains("咨询") || polished.contains("了解")) {
                polished = "您好！想咨询一下，" + polished;
            } 
            // 如果是建议类，加礼貌的开头
            else if (polished.contains("建议") || polished.contains("希望")) {
                polished = "您好！想提个建议，" + polished;
            }
            // 其他情况
            else {
                polished = "您好！" + polished;
            }
        }

        // 确保结尾有标点符号
        if (!polished.matches(".*[。！？.!?……]$")) {
            polished = polished + "。";
        }

        log.info("[本地AI]内容润色完成");
        return polished;
    }

    /**
     * 获取回复模板
     * @param content 留言内容
     * @param type 留言类型
     * @return AI 生成的回复内容
     */
    public static String generateReply(String content, String type) {
        String reply = "您好！\n\n";

        switch (type) {
            case TYPE_CONSULT:
                reply += "感谢您的咨询！我们已经收到您的留言，";
                reply += "相关同事会尽快与您联系处理。";
                break;
            case TYPE_SUGGEST:
                reply += "非常感谢您的宝贵建议！我们会认真考虑，";
                reply += "不断改进我们的产品和服务。";
                break;
            case TYPE_COMPLAINT:
                reply += "非常抱歉给您带来了不好的体验！";
                reply += "我们会尽快核实并处理您反映的问题。";
                break;
            case TYPE_THANKS:
                reply += "您的认可是我们前进的动力！";
                reply += "感谢您的支持与鼓励！";
                break;
            default:
                reply += "感谢您的留言！我们会认真阅读并处理。";
        }

        reply += "\n\n如有其他问题，欢迎随时联系我们。";
        return reply;
    }

    /**
     * 获取模板内容模板
     * @param type 类型
     * @return 模板内容
     */
    public static String getTemplate(String type) {
        switch (type) {
            case "咨询":
            case "consultation":
                return "您好！我想咨询一下关于[具体事项]的问题，请问[具体内容]。";
            case "建议":
            case "suggestion":
                return "您好！我建议可以考虑[具体建议]，这样可以让我们的服务更加完善。";
            case "感谢":
            case "praise":
                return "您好！非常感谢[感谢内容]，你们的服务真的很棒！";
            case "投诉":
            case "complaint":
                return "您好！我想反馈一个问题，关于[具体问题]，希望能得到解决。";
            default:
                return "";
        }
    }
}
