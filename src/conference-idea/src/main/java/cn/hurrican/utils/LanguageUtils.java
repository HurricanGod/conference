package cn.hurrican.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageUtils {
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String sentence = "afadasdasdasd";
        System.out.println("isContainChinese(sentence) = " + isContainChinese(sentence));
    }
}
