package common.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Errol on 2016/7/11.
 */
public class DateUtil {

    public static final String PATTERN_A = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_B = "yyyyMMddHHmmss";
    public static final String PATTERN_C = "yyyy-MM-dd";
    public static final String PATTERN_D = "yyyy年M月d日";
    public static final String PATTERN_E = "M月d日 H时m分";
    public static final String PATTERN_F = "HH:mm:ss";
    public static final String PATTERN_G = "yyyy-MM-dd  HH:mm";
    public static final String PATTERN_H = "yyyyMM";
    public static final String PATTERN_I = "yyMM";
    public static final String PATTERN_J = "yyM";
    public static final String PATTERN_K = "yy/MM/dd";

    public static String date2String(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date string2Date(String dateStr, String pattern) {
        if (dateStr.equals("")) {
            return null;
        }
        try {
            return new SimpleDateFormat(pattern).parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date stamp2Date(String timestamp) {
        if (timestamp.equals("")) {
            return null;
        }
        try {
            return new Date(Long.parseLong(timestamp));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String stamp2String(String timestamp, String pattern) {
        if (timestamp.equals("")) {
            return null;
        }
        try {
            return new SimpleDateFormat(pattern).format(new Date(Long.parseLong(timestamp)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对一个具体的时间增加时间
     *
     * @param source  需要修改的时间
     * @param hours   需要增加或者减少的小时
     * @param minutes 需要增加或者减少的分
     * @param second  需要增加或者减少的秒
     * @return {@link Date} 返回修改过的时间
     */
    public static Date addTime(Date source, int hours, int minutes, int second) {
        if (source == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(source);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        cal.add(Calendar.MINUTE, minutes);
        cal.add(Calendar.SECOND, second);
        return cal.getTime();
    }

}
