package com.xiang.proxy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

    /**
     * 默认时间格式
     */
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String weekName[] = {
            "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
    };

    /**
     * 格式化日期
     *
     * @param date    日期
     * @param pattern 格式化日期格式
     * @return 日期字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            throw new IllegalArgumentException("parameter date is required");
        }
        if (null == pattern || "".equals(pattern)) {
            pattern = FORMAT;
        }
        DateFormat df = FormatObjectReuseUtil.get(pattern);
        String formattedString =  df.format(date);
        FormatObjectReuseUtil.back(df,pattern);//对象归还
        return formattedString;
    }

    /**
     * 获取当前的日期-yyyy-MM-dd
     *
     * @return
     */
    public static long getCurrenntDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 格式化星期几
     *
     * @param date
     * @return
     */
    public static String formatWeek(Date date) {
        if (date == null) {
            return null;
        }
        return weekName[date.getDay()];
    }

    /**
     * 格式化跑步时间
     *
     * @param time
     * @return
     */
    public static String formatRunTime(Date time) {
        return time == null ? null : DateUtils.format(time, null);
    }


    public static SimpleDateFormat getSimpleDateFormat(String format) {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        return sd;
    }

    public static Date parse(String timeStr, String format) {
        if (null == format || "".equals(format)) {
            format = FORMAT;
        }
        SimpleDateFormat sd = getSimpleDateFormat(format);
        try {
            return sd.parse(timeStr);
        } catch (ParseException e) {
            //e.printStackTrace();
            //TODO
        }
        return null;
    }

    /**
     * 某天的0:0:0
     *
     * @param date
     * @return
     */
    public static Date getDawn(Date date) {
        return parseDaytime(date, 0, 0, 0);
    }

    /**
     * 某天的23:59:59
     *
     * @param date
     * @return
     */
    public static Date getBeforeDawn(Date date) {
        return parseDaytime(date, 23, 59, 59);
    }


    /**
     * 设置某天的某小时某分钟某秒
     *
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date parseDaytime(Date date, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当前时间
     *
     * @return 当前天与当前时间
     */
    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 当前天
     *
     * @return
     */
    public static Date nowDate() {
        return parse(format(now(), "yyyy-MM-dd"), "yyyy-MM-dd");
    }

    /**
     * 获取当前hour
     *
     * @return
     */
    public static int getHourOfDay() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取从某天算的第N天
     *
     * @param dayNum 小于0：前几天 大于0：之后几天
     * @return
     */
    public static Date getSomeDate(Date date, int dayNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, dayNum);
        return calendar.getTime();
    }

    /**
     * 获取从今天算的第N天
     *
     * @param dayNum
     * @return
     */
    public static Date getNextNDate(int dayNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate());
        calendar.add(Calendar.DAY_OF_MONTH, dayNum);
        return calendar.getTime();
    }

    /**
     * 获取从指定日期算的第N天
     *
     * @param dayNum
     * @return
     */
    public static Date getNextNDate(Date startDay, int dayNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDay);
        calendar.add(Calendar.DAY_OF_MONTH, dayNum);
        return calendar.getTime();
    }

    /**
     * 判断是不是今天
     *
     * @param timeStr
     * @return
     */
    public static boolean isToday(String timeStr) {
        return format(now(), "yyyy-MM-dd").equals(timeStr) || format(now(), "yyyyMMdd").equals(timeStr);
    }

    /**
     * 归一化
     *
     * @return
     */
    public static Date normal(Date date) {
        return parse(format(date, "yyyy-MM-dd"), "yyyy-MM-dd");
    }

    /**
     * 两天之间的天数,比如20160101零点到20160102零点相隔1天
     *
     * @param begin
     * @param end
     * @return
     */
    public static int getBetweenDay(Date begin, Date end) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            begin = sdf.parse(sdf.format(begin));
            end = sdf.parse(sdf.format(end));
            Calendar cal = Calendar.getInstance();
            cal.setTime(begin);
            long time1 = cal.getTimeInMillis();
            cal.setTime(end);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException ex) {
            return 0;
        }
    }

    /**
     * 获取服务器时间（GMT时间） "yyyy-MM-dd HH:mm:ss"
     */
    public static String getServerTime() {
        String str = null;
        try {
            SimpleDateFormat sdf = getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));  // 设置时区为GMT
            str = sdf.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return str;
    }

    /**
     * 判断两个时间是否间隔指定的分钟数
     *
     * @param begin    起始时间
     * @param end      结束时间
     * @param interval end - begin 单位分钟
     * @return
     */
    public static boolean isBiggerDelta(Date begin, Date end, int interval) {
        long tmp = end.getTime() - begin.getTime();
        if (tmp < 0) {
            return false;
        }
        return tmp > (interval * 60 * 1000);
    }

    public static String getDefineHourStartDateTime(int hour) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        return format(cal.getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getDefineHourEndDateTime(int hour) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        return format(cal.getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 指定日期和小时起始时间,获得开始日期和时分秒字符串
     *
     * @param cal
     * @param hour
     * @return
     */
    public static String getHourStart(Calendar cal, int hour) {
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        return format(cal.getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 指定日期和小时起始时间,获得结束日期和时分秒字符串
     *
     * @param cal
     * @param hour
     * @return
     */
    public static String getHourEnd(Calendar cal, int hour) {
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        return format(cal.getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获得一天的开始时间
     *
     * @param date 时间
     * @return
     */
    public static Date getDayStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获得一天的结束时间
     *
     * @param date 时间
     * @return
     */
    public static Date getDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 返回当前系统时间距离(单位：分钟)，今天24点剩余的时间点
     *
     * @return
     */
    public static long getLeftMinutesToToday() {
        Date endDate = parse(getDefineHourEndDateTime(23), "yyyy-MM-dd HH:mm:ss");
        return (endDate.getTime() - new Date().getTime()) / 1000;
    }

    public static boolean compare(Date perDate, Date endDate) {
        return perDate.getTime() - new Date().getTime() <= 0;
    }

    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + day);
        return now.getTime();
    }

    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) - day);
        return now.getTime();
    }

    /**
     * 返回给定时间的若干分钟后的时间
     *
     * @param date
     * @param miniute
     * @return
     */
    public static Date getMiniuteAfter(Date date, int miniute) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.MINUTE, miniute);
        return now.getTime();
    }

    /**
     * 返回两个时间点之间的秒数，注意第一个时间必须小于第二个时间，即date1<date2
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getSecondBetween(Date date1, Date date2) {
        if (date1 == null || date2 == null || date1.after(date2)) {
            return 0;
        }
        return (date2.getTime() - date1.getTime()) / 1000;
    }

    /**
     * 月
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 今年的1月1日零点
     *
     * @return
     */
    public static Date january() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now());
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return normal(cal.getTime());
    }

    /**
     * time是否在start和end之间
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isIn(Date startDate, Date endDate, Date time) {
        if (startDate == null || endDate == null || time == null) {
            return false;
        }
        return startDate.getTime() <= time.getTime() && time.getTime() <= endDate.getTime();
    }

    public static Date getDate(Date dateTime) {
        return parse(format(dateTime, "yyyy-MM-dd"), "yyyy-MM-dd");
    }


    static class FormatObjectReuseUtil{

        static String dateFormatYYYYMMDD = "yyyy-MM-dd";

        static String dateFormatYYYYHH = "yyyy-MM-dd HH:mm:ss";

        //yyyy-MM-dd
        private static Stack<DateFormat> cacheDateFormatYYYYMMDD = new Stack<>();

        //yyyy-MM-dd HH:mm:ss
        private static Stack<DateFormat> cacheDateFormatYYYYHH = new Stack<DateFormat>();


        static{
            //放入常量池
            dateFormatYYYYMMDD.intern();
            dateFormatYYYYHH.intern();

            //预先存放几个
            cacheDateFormatYYYYMMDD.push(new SimpleDateFormat(dateFormatYYYYMMDD));
            cacheDateFormatYYYYMMDD.push(new SimpleDateFormat(dateFormatYYYYMMDD));
            cacheDateFormatYYYYMMDD.push(new SimpleDateFormat(dateFormatYYYYMMDD));
            cacheDateFormatYYYYMMDD.push(new SimpleDateFormat(dateFormatYYYYMMDD));
            cacheDateFormatYYYYMMDD.push(new SimpleDateFormat(dateFormatYYYYMMDD));

            //预先存放几个
            cacheDateFormatYYYYHH.push(new SimpleDateFormat(dateFormatYYYYHH));
            cacheDateFormatYYYYHH.push(new SimpleDateFormat(dateFormatYYYYHH));
            cacheDateFormatYYYYHH.push(new SimpleDateFormat(dateFormatYYYYHH));
            cacheDateFormatYYYYHH.push(new SimpleDateFormat(dateFormatYYYYHH));
            cacheDateFormatYYYYHH.push(new SimpleDateFormat(dateFormatYYYYHH));
        }

        /**
         * 归还
         *
         * @param dateFormat
         * @param format
         */
        static void back(DateFormat dateFormat,String format){
            if(format == null || "".equals(format)){
                return ;
            }

            if(dateFormatYYYYMMDD.equals(format)){
                //对象太多，没必要放进去了
                if(cacheDateFormatYYYYMMDD.size() < 1025){
                    cacheDateFormatYYYYMMDD.push(dateFormat);
                }
            }
        }

        /**
         * 获取
         *
         * @param format
         * @return
         */
        static DateFormat get(String format){
            if(dateFormatYYYYMMDD.equals(format)){
                try {
                    return cacheDateFormatYYYYMMDD.pop();
                }catch (EmptyStackException ex){
                    return new SimpleDateFormat(format);
                }
            }
            return new SimpleDateFormat(format);
        }
    }
}
