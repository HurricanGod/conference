package cn.hurrican.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by NewObject on 2017/10/6.
 */
public class DateUtils {

    /**
     *  将 "yyyy-MM-dd" 格式的字符串转换为日期对象
     * @param datestring
     * @return 日期对象
     * @throws ParseException
     */
    public static Date convertStringToDate(String datestring) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(datestring);

        return date;
    }

    /**
     *  根据 current 的日期计算(+|-)days 后的日期
     *  days 如果为正数则返回current日期后的第days天的日期对象
     *  days 如果为负数则返回current日期前的第days天的日期对象
     *  例：如果current为 "2017-10-6", days为10,
     *  那么将返回结果为 "2017-10-16" 的日期对象
     * @param current 当前传入的日期对象
     * @param days
     * @return
     */
    public static Date calculateDateByCalendar(Date current, int days){
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(current);

        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }


    public static String convertDateToString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dtstr = df.format(date);
        return dtstr;
    }


    /**
     *  获取系统当前日期，格式为  yyyy-MM-dd
     */
    public static String getCurrentDateString(){
        return  convertDateToString(new Date());
    }

    public static String getBeforeOrAfterSomeDayDateStringFromToday(int number){
        Date date = calculateDateByCalendar(new Date(), number);
        return convertDateToString(date);
    }

    public static Date getBeforeOrAfterSomeDayFromToday(int number){
        return calculateDateByCalendar(new Date(), number);
    }
}
