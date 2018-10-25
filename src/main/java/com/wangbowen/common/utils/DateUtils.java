/**
 * Copyright &copy; 2012-2013  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.wangbowen.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author 
 * @version 2013-3-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" };

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null || StringUtils.isBlank(pattern)) {
			return null;
		}
		return DateFormatUtils.format(date, pattern, Locale.SIMPLIFIED_CHINESE);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd）
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return DateFormatUtils.format(date, "yyyy-MM-dd");
	}

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }


    public static String getNoSpSysDateString() {
        return formatDate(new Date(), "yyyyMMdd");
    }

    public static String getNoSpSysTimeString() {
        return formatDate(new Date(), "yyyyMMddHHmmss");
    }

    /**
     * 返回Date类型的SYSDATE
     *
     * @return Date型的SYSDATE
     */
    public static Date getSysDate() {
        return new Date();
    }

    /**
     * 返回Date类型的SYSDATE
     *
     * @return Date型的SYSDATE
     */
    public static Date getSysDate(String pattern) {
        return parseDate(getDate(pattern));
    }

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek(Date date) {
        return formatDate(date, "E");
    }
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

    public static Date parseDate(Object str, String parsePattern) {
        if (str == null){
            return null;
        }
        if(StringUtils.isBlank(parsePattern)){
            parsePattern="yyyy-MM-dd";
        }
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(str.toString(), parsePattern);
        } catch (ParseException e) {
            return null;
        }
    }

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}

    /**
     * 获取过去的秒
     * @param date
     * @return
     */
    public static long pastSeconds(Date date) {
        long t = new Date().getTime()-date.getTime();
        return t/1000;
    }


	/**
	 * 获取两个日期相差的天数,不考虑时分秒
	 * *如果是同一天，则相差为1
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return 天数
	 */
	public static int daysBetween(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}
		startDate = parseDate(formatDate(startDate));
		endDate = parseDate(formatDate(endDate));
		int day = (int) Math.ceil((endDate.getTime() - startDate.getTime()) / (24*60*60*1000));
		return day + 1;
	}

    /**
     * 获取当天第一时刻
     * @param date 现在时间
     * @return
     */
    public static Date getFirstTimeOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当天最后一刻
     * @param date 现在时间
     * @return
     */
    public static Date getLastTimeOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    /**
     * 获取当月第一天
     * @param date 现在时间
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当月最后一天
     * @param date 现在时间
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    /**
     * 获取上个月第一天
     * @param date 现在时间
     * @return
     */
    public static Date getLastMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    /**
     * 获取给定日期范围所包含的所有年份，形如2013,2014
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 年份列表
     */
    public static List<String> getAllYear(Date startDate, Date endDate) {
        startDate = parseDate(formatDate(startDate));
        endDate = parseDate(formatDate(endDate));

        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return null;
        }
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            int startYear = cal.get(Calendar.YEAR);
            cal.setTime(endDate);
            int endYear = cal.get(Calendar.YEAR);
            List<String> years = new ArrayList<>();
            for (int i = startYear; i <= endYear; i++) {
                years.add(i + "");
            }
            return years;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取给定日期范围所包含的所有月份，形如2013-01,2013-02
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 月份列表
     */
    public static List<String> getAllMonth(Date startDate, Date endDate, String pattern) {
        startDate = parseDate(formatDate(startDate));
        endDate = parseDate(formatDate(endDate));

        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return null;
        }

        try {
            List<String> monthList = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            int startYear = cal.get(Calendar.YEAR);
            int startMonth = cal.get(Calendar.MONTH);
            cal.setTime(endDate);
            int endYear = cal.get(Calendar.YEAR);
            int endMonth = cal.get(Calendar.MONTH);
            int months = (endYear - startYear) * 12 + (endMonth - startMonth);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            for (int i = 0; i <= months; i++) {
                monthList.add(formatDate(calendar.getTime(), pattern));
                calendar.add(Calendar.MONTH, 1);
            }
            return monthList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取给定日期范围所包含的所有星期，形如，形如2013年01周,2013年12周
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 星期列表
     */
    public static List<String> getAllWeek(Date startDate, Date endDate) {
        startDate = parseDate(formatDate(startDate));
        endDate = parseDate(formatDate(endDate));

        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return null;
        }
        try {
            List<String> weekList = new ArrayList<>();
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(startDate);
            calStart.setFirstDayOfWeek(Calendar.MONDAY);

            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(endDate);
            calEnd.setFirstDayOfWeek(Calendar.MONDAY);

            while(calStart.compareTo(calEnd) <= 0){
                int week = calStart.get(Calendar.WEEK_OF_YEAR);
                String weekStr = calStart.get(Calendar.YEAR) + "年" + (week < 10 ? "0" + week : week) + "周";
                weekList.add(weekStr);
                calStart.add(Calendar.DAY_OF_YEAR, 7);
            }
            //加上最后一天
            if (calStart.get(Calendar.WEEK_OF_YEAR) == calEnd.get(Calendar.WEEK_OF_YEAR)) {
                int week = calEnd.get(Calendar.WEEK_OF_YEAR);
                weekList.add(calEnd.get(Calendar.YEAR) + "年" + (week < 10 ? "0" + week : week) + "周");
            }
            return weekList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取给定日期范围所包含的所有日期，形如2013-01-22,2013-01-23
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日期列表
     */
    public static List<String> getAllDate(Date startDate, Date endDate) {
        startDate = parseDate(formatDate(startDate));
        endDate = parseDate(formatDate(endDate));

        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return null;
        }
        try {
            List<String> dateList = new ArrayList<>();
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(startDate);

            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(endDate);

            while(calStart.compareTo(calEnd) <= 0){
                dateList.add(formatDate(calStart.getTime()));
                calStart.add(Calendar.DAY_OF_YEAR, 1);
            }
            return dateList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 标题:    calendar2Date
     * 描述:    Calendar 转换为 Date
     * 参数:    @param calendar
     * 返回类型: Date
     * 异常:    无
     */
    public static Date calendar2Date(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        return calendar.getTime();
    }

    /**
     * 相对于当前日期的前几天(days < 0０００００)或者后几天(days > 0)
     *
     * @author wangsf
     * @param days
     * @param format
     * @param instance
     */
    @SuppressWarnings("unchecked")
    public static <T> T dayBefore2Object(int days,
                                         String format, T instance) {
        Calendar calendar = Calendar.getInstance();
        if (format == null || format.equals("")) {
            format = parsePatterns[1];
        }
        if (instance instanceof Date) {
            calendar.setTime((Date) instance);
            calendar.add(Calendar.DATE, days);// 增加x天
            Date date = calendar.getTime();
            return (T) date;
        } else if (instance instanceof String) {
            calendar.add(Calendar.DATE, days);
            return (T) formatDate(calendar2Date(calendar), format);
        }
        return null;
    }

    /**
     * 相对于当前日期的前几天(days < 0０００００)或者后几天(days > 0)
     *
     * @param days
     * @return
     */
    public static Date dayBefore2Date(int days) {
        Date date = new Date();
        return dayBefore2Object(days, null, date);
    }

    /**
     * 相对于当前日期的前几分钟(seconds < 0０００００)或者后几分钟(seconds > 0)
     *
     * @param minuts
     * @param format
     * @param instance
     */
    @SuppressWarnings("unchecked")
    public static <T> T minutBefore2Object(int minuts,
                                           String format, T instance) {
        Calendar calendar = Calendar.getInstance();
        if (format == null || format.equals("")) {
            format = parsePatterns[1];
        }
        if (instance instanceof Date) {
            calendar.setTime((Date) instance);
            calendar.add(Calendar.MINUTE, minuts);// 增加x分钟
            Date date = calendar.getTime();
            return (T) date;
        } else if (instance instanceof String) {
            calendar.add(Calendar.MINUTE, minuts);
            return (T) formatDate(calendar2Date(calendar), format);
        }
        return null;
    }

    /**
     * 相对于当前日期的前几分钟(days < 0０００００)或者后几分钟(days > 0)
     *
     * @param minuts
     * @return
     */
    public static Date minutBefore2Date(int minuts, Date date) {
        return minutBefore2Object(minuts, null, date);
    }


    /**
     * 相对于当前日期的前几月(months < 0０００００)或者后几月(months > 0)时间
     *
     * @param <T>
     * @param months
     * @param format
     * @param instance
     */
    @SuppressWarnings("unchecked")
    public static <T extends Object> T monthBefore2Object(int months,
                                                          String format, T instance) {
        if (months == 0) {
            return null;
        }

        if (format == null || format.equals("")) {
            format = parsePatterns[1];
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, months);

        if (instance instanceof Date) {
            return (T) calendar.getTime();
        } else if (instance instanceof String) {
            return (T) formatDate(calendar2Date(calendar), format);
        }

        return null;
    }

    /**
     * 相对于当前日期的前几月(months < 0０００００)或者后几月(months > 0)时间
     *
     * @param months
     * @return
     */
    public static Date monthBefore2Date(int months) {
        Date date = new Date();
        return monthBefore2Object(months, null, date);
    }
    
    /** 
     * 获取随机日期 
     *  
     * @param beginDate 
     *            起始日期，格式为：yyyy-MM-dd 或  yyyy-MM-dd HH:mm:ss
     * @param endDate 
     *            结束日期，格式为：yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss
     * @return 
     */  
  
    public static Date randomDate(String beginDate, String endDate, String datePatterns) {
        try {  
            
            if(StringUtils.isBlank(datePatterns)){
                datePatterns = "yyyy-MM-dd";
            }
            SimpleDateFormat format = new SimpleDateFormat(datePatterns);
            Date start = format.parse(beginDate);// 构造开始日期
            Date end = format.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。  
            if (start.getTime() >= end.getTime()) {  
                return null;  
            }  
            long date = random(start.getTime(), end.getTime());  
  
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    public static long random(long begin, long end) {  
        long rtn = begin + (long) (Math.random() * (end - begin));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值  
        if (rtn == begin || rtn == end) {  
            return random(begin, end);  
        }  
        return rtn;  
    } 
    
    /**
     * 根据传入的日期以及月份数计算并返回日期
     * 
     * @param date
     * @param months
     * @return
     * @see 
     * @since [1.0]
     */
    public static Date addMonth(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);

        return cal.getTime();
    }
    
    /**
     * 根据传入的日期以及天数计算并返回日期
     * 
     * @param date
     * @param hours
     * @return
     * @see 
     * @since [1.0]
     */
    public static Date addDate(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    /**
     * 根据传入的日期以及分钟数计算并返回日期
     * 
     * @param date
     * @param months
     * @return
     * @see 
     * @since [1.0]
     */
    public static Date addMinute(Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);

        return cal.getTime();
    }

    /**
     * 根据传入的日期以及小时数计算并返回日期
     * 
     * @param date
     * @param hours
     * @return
     * @see 
     * @since [1.0]
     */
    public static Date addHour(Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hours);

        return cal.getTime();
    }

    /**
     * 根据传入的日期以及年数计算并返回日期
     * 
     * @param date
     * @param years
     * @return
     * @see 
     * @since [1.0]
     */
    public static Date addYear(Date date, int years) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);

        return cal.getTime();
    }

    /**
     * 根据传入的日期以及星期数计算并返回日期
     * 
     * @param date
     * @param years
     * @return
     * @see 
     * @since [1.0]
     */
    public static Date addWeek(Date date, int weeks) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.WEEK_OF_MONTH, weeks);

        return cal.getTime();
    }
    
    /**
     * 将String类型日期转换为Date类型的日期
     * @param strDate
     * @param formatter
     * @return
     * @see 
     * @since [1.0]
     */
    public static Date parseDateByStr(String strDate, String formatter) {
        if(formatter == null || formatter.trim().equals("")){
            formatter = "yyyy-MM-dd";
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(formatter);
        //必须捕获异常
        try{ 
           Date date=simpleDateFormat.parse(strDate);
           return date;
        }catch(ParseException ex){
           ex.printStackTrace();
        }
        return null;
    }
    
    /**
     * 根据传入的日期时间字符串生成日期对象 传入的日期字符串格式为 YYYY-MM-DD hh:mm:ss
     * 如果hh:mm:ss没有设置，默认为00:00:00
     * 
     * @param str
     * @return
     * @see 
     * @since [1.0]
     */
    public static Date parseDateByStr(String str) {
        Calendar cal = Calendar.getInstance();
        if (str == null) {
            return null;
        }

        String dateArray[] = str.split("-");
        if (dateArray.length != 3) {
            return null;
        }

        int year = Integer.parseInt(dateArray[0]);
        int month = Integer.parseInt(dateArray[1]);
        int date = 1;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (dateArray[2].indexOf(":") > 0) {
            int i = dateArray[2].indexOf(" ");
            date = Integer.parseInt(dateArray[2].substring(0, i));
            if (i > 0) {
                String hmStr = dateArray[2].substring(i + 1);
                String[] hmArray = hmStr.split(":");
                if (hmArray.length >= 2) {
                    hour = Integer.parseInt(hmArray[0]);
                    minute = Integer.parseInt(hmArray[1]);
                    if (hmArray.length == 3) {
                        second = Integer.parseInt(hmArray[2]);
                    }
                }
            }
        } else {
            date = Integer.parseInt(dateArray[2]);
        }

        cal.set(year, month - 1, date, hour, minute, second);

        return cal.getTime();
    }
    
    /**
     * 取得两个时间相隔的天数
     * 
     * @param startDate
     * @param endDate
     * @return
     * @see 
     * @since [1.0]
     */
    public static int getIntervalDays(Date startDate, Date endDate) {
        long start = startDate.getTime();
        long end = endDate.getTime();

        int residue =  (int)(Math.abs(end - start) % (24 * 60 * 60 * 1000));
        if(residue > 0) {
            return ((int)(Math.abs(end - start) / (24 * 60 * 60 * 1000))) + 1;
        }else {
            return ((int)(Math.abs(end - start) / (24 * 60 * 60 * 1000)));
        }
    }
    
    /**
     * 取得两个时间总的天数，包含头尾
     * 
     * @param startDate 只支持yyyy-MM-dd格式的日期
     * @param endDate 只支持yyyy-MM-dd格式的日期
     * @return
     * @see 
     * @since [1.0]
     */
    public static int getBetweenDayNum(Date startDate, Date endDate) {
        long start = startDate.getTime();
        long end = endDate.getTime();

        return (int)(Math.abs(end - start) / (24 * 60 * 60 * 1000)) + 1;
    }
    
    /** 
     * 获取某年某月某日是星期几  参数格式:yyyy-MM-mm
     * */  
    public static String getWeekDayByYearMonthDay(String s){
        
        Calendar c = Calendar.getInstance(Locale.CHINA);
        String[] sp = s.split("-");
        c.set(Calendar.YEAR, Integer.parseInt(sp[0]));
        c.set(Calendar.MONTH, Integer.parseInt(sp[1])-1);
        c.set(Calendar.DATE, Integer.parseInt(sp[2]));

        int wd = c.get(Calendar.DAY_OF_WEEK);
        String x = "";
        switch(wd){
            case 1:x="星期日";break;
            case 2:x="星期一";break;
            case 3:x="星期二";break;
            case 4:x="星期三";break;
            case 5:x="星期四";break;
            case 6:x="星期五";break;
            case 7:x="星期六";break;
        }

        return x;
    }

    /**
     * 根据日期计算年龄
     * @param oBirthDay
     * @return int (年龄)
     */

    public static int getAge(Date oBirthDay) {
        int iAge;
        Calendar oCalendarToday = Calendar.getInstance();
        oCalendarToday.setTime(new Date());

        Calendar oCalendarBirthday = Calendar.getInstance();
        oCalendarBirthday.setTime(oBirthDay);

        Calendar oCalendarTemp = Calendar.getInstance();
        oCalendarTemp.set(oCalendarToday.get(Calendar.YEAR), oCalendarBirthday
                .get(Calendar.MONTH), oCalendarBirthday.get(Calendar.DATE));

        iAge = oCalendarToday.get(Calendar.YEAR)
                - oCalendarBirthday.get(Calendar.YEAR);
        if (!oCalendarToday.after(oCalendarTemp)) {
            iAge--;
        }
        if (iAge == 0) {
            iAge = 1;
        }
        return iAge;
    }
    
    /**
     * 获取上个月第一天的“yyyy-MM-dd”格式字符串 : <br>
     * 
     *
     * @return
     * @see 
     * @since [1.0]
     */
    public static String getFirstDayOfLastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return formatDate(cal.getTime());
    }
    
    /**
     * 获取上个月最后一天的“yyyy-MM-dd”格式字符串 : <br>
     * 
     *
     * @return
     * @see 
     * @since [1.0]
     */
    public static String getLastDayOfLastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return formatDate(cal.getTime());
    }

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));

//		Date startDate = parseDate("2015-02-09", "yyyy-MM-dd");
//		Date endDate = parseDate("2015-03-02", "yyyy-MM-dd");
//		System.out.println(getAllDate(startDate, endDate));

        System.out.println(getFirstDayOfLastMonth());
        System.out.println(getLastDayOfLastMonth());
    }
}
