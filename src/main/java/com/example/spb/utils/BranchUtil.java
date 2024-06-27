package com.example.spb.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BranchUtil {
    public static int convert(String input) {
        // 定义正则表达式模式，匹配字符串中的数字
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            // 提取并返回匹配的数字部分
            return Integer.parseInt(matcher.group());
        } else {
            throw new IllegalArgumentException("输入字符串中不包含数字。");
        }
    }
}
