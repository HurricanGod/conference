package cn.hurrican.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    public static final String url_regex = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
            + "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
            + "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
            + "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"
            + "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"
            + "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
            + "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"
            + "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$";

    /** 发布会议：网址正则表达式 **/
    public static final String simple_url_regex = "^[a-zA-Z0-9\\:/]+\\..*$";

    /**  **/
    public static final String regEx_html_tag = "<[^>]+>"; // HTML tag

    private static Pattern pattern = Pattern.compile(url_regex);
    private static Pattern simpleUrlPattern = Pattern.compile(simple_url_regex);
    private static Pattern htmlTagPattern = Pattern.compile(regEx_html_tag);


    public static Boolean isLegalSimpleUrl(String url){
        Matcher matcher = simpleUrlPattern.matcher(url);
        return matcher.matches();
    }

    public static Boolean isLegalUrl(String url){
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    public static void main(String[] args) {
        String url = "www.baidu.com";
        System.out.println("isLegalUrl(url) = " + isLegalUrl(url));
    }
}
