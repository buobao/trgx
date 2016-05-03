package com.joint.base.util;

import org.apache.commons.lang.time.DateUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtil extends DateUtils {

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	
	/**
	 * 功能: 将日期对象按照某种格式进行转换，返回转换后的字符串
	 * 
	 * @param date 日期对象
	 * @param pattern 转换格式 例：yyyy-MM-dd
	 */
	public static String DateToString(Date date, String pattern) {
		String strDateTime = null;
		SimpleDateFormat formater = new SimpleDateFormat(pattern);
		strDateTime = date == null ? null : formater.format(date);
		return strDateTime;
	}

	/**
	 * 功能: 将传入的日期对象按照yyyy-MM-dd格式转换成字符串返回
	 * 
	 * @param date 日期对象
	 * @return String
	 */
	public static String DateToString(Date date) {
		String _pattern = "yyyy-MM-dd";
		return date == null ? null : DateToString(date, _pattern);
	}

	/**
	 * 功能: 将传入的日期对象按照yyyy-MM-dd HH:mm:ss格式转换成字符串返回
	 * 
	 * @param date 日期对象
	 * @return String
	 */
	public static String DateTimeToString(Date date) {
		String _pattern = "yyyy-MM-dd HH:mm:ss";
		return date == null ? null : DateToString(date, _pattern);
	}
	
	/**
	 * 功能: 将传入的日期对象按照yyyy-MM-dd HH:mm格式转换成字符串返回
	 * 
	 * @param date 日期对象
	 * @return String
	 */
	public static String DateMinToString(Date date) {
		String _pattern = "yyyy-MM-dd HH:mm";
		return date == null ? null : DateToString(date, _pattern);
	}

	/**
	 * 功能: 将插入的字符串按格式转换成对应的日期对象
	 * 
	 * @param str 字符串
	 * @param pattern 格式
	 * @return Date
	 */
	public static Date StringToDate(String str, String pattern) {
		Date dateTime = null;
		try {
			if (str != null && !str.equals("")) {
				SimpleDateFormat formater = new SimpleDateFormat(pattern);
				dateTime = formater.parse(str);
			}
		} catch (Exception ex) {
		}
		return dateTime;
	}

	/**
	 * 功能: 将传入的字符串按yyyy-MM-dd格式转换成对应的日期对象
	 * 
	 * @param str 需要转换的字符串
	 * @return Date 返回值
	 */
	public static Date StringToDate(String str) {
		String _pattern = "yyyy-MM-dd";
		return StringToDate(str, _pattern);
	}

	/**
	 * 功能: 将传入的字符串按yyyy-MM-dd HH:mm:ss格式转换成对应的日期对象
	 * 
	 * @param str 需要转换的字符串
	 * @return Date
	 */
	public static Date StringToDateTime(String str) {
		String _pattern = "yyyy-MM-dd HH:mm:ss";
		return StringToDate(str, _pattern);
	}

	/**
	 * 功能: 将传入的字符串转换成对应的Timestamp对象
	 * 
	 * @param str 待转换的字符串
	 * @return Timestamp 转换之后的对象
	 * @throws Exception
	 *             Timestamp
	 */
	public static Timestamp StringToDateHMS(String str) throws Exception {
		Timestamp time = null;
		time = Timestamp.valueOf(str);
		return time;
	}

	/**
	 * 功能: 根据传入的年月日返回相应的日期对象
	 * 
	 * @param year 年份
	 * @param month 月份
	 * @param day 天
	 * @return Date 日期对象
	 */
	public static Date YmdToDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		return calendar.getTime();
	}

	/**
	 * 功能: 将日期对象按照MM/dd HH:mm:ss的格式进行转换，返回转换后的字符串
	 * 
	 * @param date 日期对象
	 * @return String 返回值
	 */
	public static String communityDateToString(Date date) {
		SimpleDateFormat formater = new SimpleDateFormat("MM/dd HH:mm:ss");
		String strDateTime = date == null ? null : formater.format(date);
		return strDateTime;
	}

	public static Date getMaxDateOfDay(Date date) {
		if (date == null) {
			return null;
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(11, calendar.getActualMaximum(11));
			calendar.set(12, calendar.getActualMaximum(12));
			calendar.set(13, calendar.getActualMaximum(13));
			calendar.set(14, calendar.getActualMaximum(14));
			return calendar.getTime();
		}
	}
	public static Date getMaxDateOfMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
	public static Date getMinDateOfMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH,0);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		return calendar.getTime();
	}
	public static Date getMinDateOfDay(Date date) {
		if (date == null) {
			return null;
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(11, calendar.getActualMinimum(11));
			calendar.set(12, calendar.getActualMinimum(12));
			calendar.set(13, calendar.getActualMinimum(13));
			calendar.set(14, calendar.getActualMinimum(14));
			return calendar.getTime();
		}
	}
	/**
	 * 功能：返回传入日期对象（date）之后afterDays天数的日期对象
	 * 
	 * @param date 日期对象
	 * @param afterYears 往后天数
	 * @return java.util.Date 返回值
	 */
	public static Date getAfterYears(Date date, int afterYears) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, afterYears);
		return cal.getTime();
	}
	/**
	 * 功能：返回传入日期对象（date）之后afterDays天数的日期对象
	 * 
	 * @param date 日期对象
	 * @param afterMonths 往后天数
	 * @return java.util.Date 返回值
	 */
	public static Date getAfterMonths(Date date, int afterMonths) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, afterMonths);
		return cal.getTime();
	}
	/**
	 * 功能：返回传入日期对象（date）之后afterDays天数的日期对象
	 * 
	 * @param date 日期对象
	 * @param afterDays 往后天数
	 * @return java.util.Date 返回值
	 */
	public static Date getAfterDay(Date date, int afterDays) {
        if(date!=null && !date.equals("")){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, afterDays);
            return cal.getTime();
        }else{
            return null;
        }
	}
	/**
	 * 功能：返回传入日期对象（date）之后afterDays天数的日期对象
	 * 
	 * @param date 日期对象
	 * @param afterMinutes 往后天数正数，否则负数
	 * @return java.util.Date 返回值
	 */
	public static Date getAfterMinutes(Date date, int afterMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, afterMinutes);
		return cal.getTime();
	}
	// day
	/**
	 * 功能: 返回date1与date2相差的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return int
	 */
	public static int DateDiff(Date date1, Date date2) {
		int i = (int) ((date1.getTime() - date2.getTime()) / 3600 / 24 / 1000);
		return i;
	}

	public static int YearDiff(Date date1, Date date2) {
		int i = (int) ((date1.getTime() - date2.getTime()) / 3600 / 24 / 1000 /365);
		return i;
	}
	/**
	 * 功能: 返回date1与date2相差的月数
	 *
	 * @param date1
	 * @param date2
	 * @return int
	 */
	public static int MonthDiff(Date date1, Date date2) {
		int i = (int) ((date1.getTime() - date2.getTime()) / 3600 / 24 / 1000 /12);
		return i;
	}
	// min
	/**
	 * 功能: 返回date1与date2相差的分钟数
	 * 
	 * @param date1
	 * @param date2
	 * @return int
	 */
	public static long MinDiff(Date date1, Date date2) {
		long i = (long) ((date1.getTime() - date2.getTime()) / 1000 / 60);
		return i;
	}

	// second
	/**
	 * 功能: 返回date1与date2相差的毫秒数
	 * 
	 * @param date1
	 * @param date2
	 * @return int
	 */
	public static long TimeDiff(Date date1, Date date2) {
		long i = (long) ((date1.getTime() - date2.getTime()));
		return i;
	}
	
	/**
	 * 以友好的方式显示时间
	 * @param time
	 * @return
	 */
	public static String friendly_time(Date time) {
		if(time == null) {
			return "";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();
		
		//判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if(curDate.equals(paramDate)){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
			else 
				ftime = hour+"小时前";
			return ftime;
		}
		
		long lt = time.getTime()/86400000;
		long ct = cal.getTimeInMillis()/86400000;
		int days = (int)(ct - lt);		
		if(days == 0){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
			else 
				ftime = hour+"小时前";
		}
		else if(days == 1){
			ftime = "昨天";
		}
		else if(days == 2){
			ftime = "前天";
		}
		else if(days > 2 && days <= 10){ 
			ftime = days+"天前";			
		}
		else if(days > 10){			
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

    /**
     * 通过给定的日期,得到该日期所在月份天数
     * @param date
     * @return
     */
    public static String[] getMonthDays(String date) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
        calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7)) -1);

        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String[] days = new String[maxDay];

        for(int i = 1; i <= maxDay; i++){
            days[i-1] = String.valueOf(i);
        }

        return days;
    }
}
