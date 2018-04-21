package cn.hurrican.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: Hurrican
 * @Description:
 * @Date 2018/1/25
 * @Modified 9:46
 */
public class DateTimeUtils {

    private static final String pattern = "yyyy-MM-dd HH:mm:ss";

    public static Date addSpecifiedSecondToDate(Date date, int second){
        if(date == null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * @param dateString yyyy-MM-dd HH:mm:ss格式的字符串
     * @param second     相对参数dateString的秒偏移量
     * @return 目标日期 或 null(发生异常时返回null)
     */
    public static Date addSpecifiedSecondToDate(String dateString, int second) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        boolean happenException = false;
        Date item = null;
        try {
            Date date = dateFormat.parse(dateString);
            item = addSpecifiedSecondToDate(date, second);
        } catch (ParseException e) {
            happenException = true;
            throw new RuntimeException(e);
        } finally {
            if (happenException) {
                return null;
            }
            return item;
        }
    }

    /**
     * 设当前时间戳对应的时间为 "2018-1-25 10:43:14" ,second = 16; return "2018-1-25 10:43:30"
     * @param startTimestamp 时间戳
     * @param seconds 秒数（可正可负）
     * @return Date 将当前时间戳加上或减去指定秒数的Date
     */
    public static Date addSpecifiedSecondToDate(long startTimestamp, int seconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTimestamp);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }
    
    public static String format(Date date) {
		return new SimpleDateFormat(pattern).format(date);
	}

    public static long parseDateToTimestamp(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }
}
