package com.lawrence.core.lib.utils.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 * <p/>
 * Created by xu on 2015/8/14.
 */
public class DateUtil {

    // 获取日历实例
    private static Calendar calendar = Calendar.getInstance();

    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;

    static private final String SUNDAY = "星期日";
    static private final String MONDAY = "星期一";
    static private final String TUESDAY = "星期二";
    static private final String WEDNESDAY = "星期三";
    static private final String THURSDAY = "星期四";
    static private final String FRIDAY = "星期五";
    static private final String SATURDAY = "星期六";


    public enum DateStyle {
        MMdd_HH_MM("MMdd_HH:mm"),
        YYYYMMDDhhmmss("yyyyMMddHHmmss"),
        YYYYMMDDhhmmssss("yyyyMMddHHmmssss"),
        YYYYMMDD("yyyyMMdd"),
        YYYY_MM_DD("yyyy-MM-dd"),
        YYYYMMDDPY("yyyy年MM月dd日"),
        YYYYMMDDHHMMPY("yyyy年MM月dd日 HH:mm"),
        YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
        HHMMSS("HH:mm:dd"),
        HHMMSSPY("HH时mm分ss秒");


        private String dateStyle = "";

        DateStyle(String style) {
            this.dateStyle = style;
        }

        public String getDateStyle() {
            return this.dateStyle;
        }
    }


    /**
     * 获取当前日期
     *
     * @return 当前日期 YYYY-MM-DD
     */
    public static String getDate() {
        return getYear() + "-" + getMonth() + "-" + getDay();
    }

    /**
     * 通过格式化,获取想要的时间格式
     *
     * @param format 时间格式, 通过枚举传递, 也可自行传入
     * @return 格式化后的时间, String 类型
     */
    public static String getDate(String format) {
        calendar.setTime(new Date());
        return new SimpleDateFormat(format, Locale.CHINA).format(calendar.getTime());
    }

    /**
     * 通过格式化,获取想要的时间格式
     *
     * @param format 时间格式, 通过枚举传递, 也可自行传入
     * @return 格式化后的时间, String 类型
     */
    public static String getDate(DateStyle format) {
        return getDate(format.getDateStyle());
    }


    /**
     * 获取当前日期和星期
     * <p/>
     * yyyy年mm月dd日 星期一
     *
     * @return 当前结果
     */

    public static String getDateAndWeek() {
        return SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL).format(calendar.getTime());
    }

    /**
     * 距离今天多久
     *
     * @param date
     * @return
     */
    public static String fromToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        long time = date.getTime() / 1000;
        long now = new Date().getTime() / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟前";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE)
                    + "分钟前";
        else if (ago <= ONE_DAY * 2)
            return "昨天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
                    + calendar.get(Calendar.MINUTE) + "分";
        else if (ago <= ONE_DAY * 3)
            return "前天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
                    + calendar.get(Calendar.MINUTE) + "分";
        else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            return day + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
                    + calendar.get(Calendar.MINUTE) + "分";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            return month + "个月" + day + "天前"
                    + calendar.get(Calendar.HOUR_OF_DAY) + "点"
                    + calendar.get(Calendar.MINUTE) + "分";
        } else {
            long year = ago / ONE_YEAR;
            int month = calendar.get(Calendar.MONTH) + 1;// JANUARY which is 0 so month+1
            return year + "年前" + month + "月" + calendar.get(Calendar.DATE)
                    + "日";
        }

    }

    /**
     * 距离截止日期还有多长时间
     *
     * @param date
     * @return
     */
    public static String fromDeadline(Date date) {
        long deadline = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long remain = deadline - now;
        if (remain <= ONE_HOUR)
            return "只剩下" + remain / ONE_MINUTE + "分钟";
        else if (remain <= ONE_DAY)
            return "只剩下" + remain / ONE_HOUR + "小时"
                    + (remain % ONE_HOUR / ONE_MINUTE) + "分钟";
        else {
            long day = remain / ONE_DAY;
            long hour = remain % ONE_DAY / ONE_HOUR;
            long minute = remain % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return "只剩下" + day + "天" + hour + "小时" + minute + "分钟";
        }

    }

    /**
     * 距离今天的绝对时间
     *
     * @param date
     * @return
     */
    public static String toToday(Date date) {
        long time = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE) + "分钟";
        else if (ago <= ONE_DAY * 2)
            return "昨天" + (ago - ONE_DAY) / ONE_HOUR + "点" + (ago - ONE_DAY)
                    % ONE_HOUR / ONE_MINUTE + "分";
        else if (ago <= ONE_DAY * 3) {
            long hour = ago - ONE_DAY * 2;
            return "前天" + hour / ONE_HOUR + "点" + hour % ONE_HOUR / ONE_MINUTE
                    + "分";
        } else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            long hour = ago % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return day + "天前" + hour + "点" + minute + "分";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            long hour = ago % ONE_MONTH % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_MONTH % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return month + "个月" + day + "天" + hour + "点" + minute + "分前";
        } else {
            long year = ago / ONE_YEAR;
            long month = ago % ONE_YEAR / ONE_MONTH;
            long day = ago % ONE_YEAR % ONE_MONTH / ONE_DAY;
            return year + "年前" + month + "月" + day + "天";
        }

    }

    public static String getYear() {
        return calendar.get(Calendar.YEAR) + "";
    }

    public static String getMonth() {
        int month = calendar.get(Calendar.MONTH) + 1;
        return month + "";
    }

    public static String getDay() {
        return calendar.get(Calendar.DATE) + "";
    }

    public static String get24Hour() {
        return calendar.get(Calendar.HOUR_OF_DAY) + "";
    }

    public static String getMinute() {
        return calendar.get(Calendar.MINUTE) + "";
    }

    public static String getSecond() {
        return calendar.get(Calendar.SECOND) + "";
    }

    /**
     * 获取当前是星期几
     *
     * @return 星期日, 星期一, 星期二, 星期三, 星期四, 星期五, 星期六
     */
    public static String getDayOfTheWeek() {
        return getDayOfTheWeek(calendar);
    }

    private static String getDayOfTheWeek(Calendar calendar) {
        String weekend;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                weekend = SUNDAY;
                break;
            case 2:
                weekend = MONDAY;
                break;
            case 3:
                weekend = TUESDAY;
                break;
            case 4:
                weekend = WEDNESDAY;
                break;
            case 5:
                weekend = THURSDAY;
                break;
            case 6:
                weekend = FRIDAY;
                break;
            case 7:
                weekend = SATURDAY;
                break;
            default:
                weekend = "";
                break;
        }
        return weekend;
    }

    /**
     * 获取日期是星期几
     *
     * @return 星期日, 星期一, 星期二, 星期三, 星期四, 星期五, 星期六
     */
    public static String getDayOfTheWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getDayOfTheWeek(calendar);
    }
}
