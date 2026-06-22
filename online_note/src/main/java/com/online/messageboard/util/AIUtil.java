package com.online.messageboard.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AIUtil {

    private static final Logger logger = LoggerFactory.getLogger(AIUtil.class);

    private static final String[] SENSITIVE_WORDS = {
        "脏话", "粗口", "f**k", "shit", "damn", "垃圾", "白痴", "废物", "sb", "傻逼",
        "滚", "去死", "fuck", "bullshit", "草泥马", "操", "日", "他妈", "他妈",
        "恶心", "贱人", "混蛋", "蠢", "笨", "垃圾", "废物", "没用", "去死吧",
        "滚蛋", "智障", "脑残", "有病", "神经病", "疯子", "傻逼", "狗娘养的",
        "操你", "干你", "日你", "草你", "肏", "去你妈", "去你大爷",
        "他妈", "他妈的", "去死", "滚蛋", "fuck", "shit", "cunt", "pussy",
        "dick", "cock", "twat", "ass", "asshole", "bitch", "whore", "slut"
    };

    private static final String[] CONSULT_KEYWORDS = {"请问", "咨询", "怎么", "如何", "哪里", "什么", "多少", "能否", "吗", "？", "疑问", "想知道", "请教", "问一下", "询问", "想了解"};
    private static final String[] SUGGEST_KEYWORDS = {"建议", "希望", "希望能够", "希望可以", "应该", "最好", "改进", "优化", "增加", "添加", "建议您", "建议贵", "可以考虑", "能否增加"};
    private static final String[] COMPLAINT_KEYWORDS = {"投诉", "不满", "失望", "太差", "垃圾", "问题", "bug", "闪退", "崩溃", "慢", "难用", "不好", "糟糕", "气愤", "生气"};
    private static final String[] THANKS_KEYWORDS = {"感谢", "谢谢", "非常满意", "特别好", "太棒了", "好评", "满意", "很好", "不错", "优秀"};

    public static boolean checkRiskContent(String content) {
        return localCheckRiskContent(content);
    }

    public static String classifyContent(String content) {
        return localClassifyContent(content);
    }

    public static String polishContent(String content) {
        return localPolishContent(content);
    }

    public static String generateReply(String messageContent, String messageType) {
        return localGenerateReply(messageContent, messageType);
    }

    private static boolean localCheckRiskContent(String content) {
        if (content == null || content.trim().isEmpty()) return false;
        String lowerContent = content.toLowerCase();
        for (String keyword : SENSITIVE_WORDS) {
            if (lowerContent.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private static String localClassifyContent(String content) {
        if (content == null || content.trim().isEmpty()) return "其他";
        int consultScore = 0, suggestScore = 0, complaintScore = 0, thanksScore = 0;
        String lowerContent = content.toLowerCase();

        for (String k : CONSULT_KEYWORDS) {
            if (lowerContent.contains(k.toLowerCase())) {
                consultScore += 2;
            }
        }
        for (String k : SUGGEST_KEYWORDS) {
            if (lowerContent.contains(k.toLowerCase())) {
                suggestScore += 2;
            }
        }
        for (String k : COMPLAINT_KEYWORDS) {
            if (lowerContent.contains(k.toLowerCase())) {
                complaintScore += 2;
            }
        }
        for (String k : THANKS_KEYWORDS) {
            if (lowerContent.contains(k.toLowerCase())) {
                thanksScore += 2;
            }
        }

        Map<String, Integer> scoreMap = new HashMap<>();
        scoreMap.put("咨询", consultScore);
        scoreMap.put("建议", suggestScore);
        scoreMap.put("投诉", complaintScore);
        scoreMap.put("感谢", thanksScore);

        int maxScore = Collections.max(scoreMap.values());
        if (maxScore > 0) {
            for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
                if (entry.getValue() == maxScore) {
                    return entry.getKey();
                }
            }
        }
        return "其他";
    }

    private static String localPolishContent(String content) {
        if (content == null || content.trim().isEmpty()) return content;

        String result = content;
        result = result.replaceAll("\\s+", " ");
        result = result.trim();

        result = result.replaceAll("，，", "，");
        result = result.replaceAll("。。", "。");
        result = result.replaceAll("！！", "！");
        result = result.replaceAll("？？", "？");

        if (result.length() < 20) {
            if (result.contains("怎么") || result.contains("如何") || result.contains("什么")) {
                if (!result.endsWith("？")) {
                    result = result + "，请问这个问题该怎么解决呢？";
                }
            } else if (result.contains("建议")) {
                if (!result.endsWith("。")) {
                    result = result + "，希望能被采纳！";
                }
            } else if (result.contains("感谢") || result.contains("谢谢")) {
                if (!result.endsWith("！")) {
                    result = result + "，非常感谢！";
                }
            } else {
                if (!result.endsWith("。")) {
                    result = result + "。";
                }
            }
        }

        if (!result.endsWith("。") && !result.endsWith("！") && !result.endsWith("？") && !result.endsWith("~")) {
            result = result + "。";
        }

        return result;
    }

    private static String localGenerateReply(String messageContent, String messageType) {
        if (messageContent == null || messageContent.trim().isEmpty()) {
            return "感谢您的留言，我们会尽快处理。如有其他问题，请随时联系我们。";
        }

        String type = messageType != null ? messageType : "其他";
        StringBuilder reply = new StringBuilder();
        reply.append("您好！感谢您的留言，");

        switch (type) {
            case "咨询":
                reply.append("针对您咨询的问题，我们非常重视！\n\n");
                reply.append("1. 感谢您对我们产品/服务的关注和信任。\n");
                reply.append("2. 针对您提到的问题，我们会安排专业人员与您联系。\n");
                reply.append("3. 如需更详细的解答，请拨打我们的客服热线：400-888-8888。\n");
                reply.append("4. 您也可以通过官网在线客服进行咨询，我们会在第一时间为您服务。\n");
                reply.append("\n感谢您的理解与支持，祝您生活愉快！");
                break;
            case "建议":
                reply.append("非常感谢您提出的宝贵建议！\n\n");
                reply.append("1. 您的建议已被我们认真记录，相关部门会仔细评估。\n");
                reply.append("2. 我们会将您的建议纳入产品改进计划。\n");
                reply.append("3. 如果您的建议被采纳，我们将有机会为您提供精美礼品一份。\n");
                reply.append("\n感谢您对我们工作的支持与信任！您的建议是我们进步的动力！");
                break;
            case "投诉":
                reply.append("对于给您带来的不便，我们深表歉意！\n\n");
                reply.append("1. 我们已将您的问题反馈给相关责任部门，正在加急处理。\n");
                reply.append("2. 我们会立即安排专人跟进处理，争取尽快解决您的问题。\n");
                reply.append("3. 处理结果会在第一时间与您沟通，请您耐心等待。\n");
                reply.append("4. 如有进一步疑问，请随时联系我们，我们竭诚为您服务。\n");
                reply.append("\n再次感谢您的理解与支持！我们会努力改进！");
                break;
            case "感谢":
                reply.append("非常感谢您的认可与好评！\n\n");
                reply.append("1. 您的满意是我们最大的追求！\n");
                reply.append("2. 我们会继续努力，为您提供更优质的产品和服务。\n");
                reply.append("3. 期待与您下次相遇，祝您生活愉快！\n");
                reply.append("\n再次感谢您的支持，我们会做得更好！");
                break;
            default:
                reply.append("我们已收到您的留言！\n\n");
                reply.append("1. 感谢您对我们的关注与支持！\n");
                reply.append("2. 我们会认真对待每一条留言。\n");
                reply.append("3. 如需进一步沟通，请随时联系我们。\n");
                reply.append("\n祝您生活愉快，万事顺心！");
                break;
        }
        return reply.toString();
    }

    public static String getTemplate(String type) {
        switch (type) {
            case "consult":
                return "您好，我想咨询一下关于【您的问题】的问题，请问贵公司能提供相关的服务吗？具体情况是这样的：【详细描述】。期待您的回复，谢谢！";
            case "suggest":
                return "您好，我对贵公司的产品/服务有一些建议：\n1. 建议【具体建议1】\n2. 希望能够【具体建议2】\n3. 建议可以【具体建议3】\n希望我的建议对您有所帮助，希望贵公司越办越好，谢谢！";
            case "thanks":
                return "您好，我非常满意贵公司的产品/服务，特别是【具体满意的功能】非常好用！感谢工作人员的认真负责，这次体验让我很愉快。祝公司越办越好，生意兴隆！";
            default:
                return "";
        }
    }
}
