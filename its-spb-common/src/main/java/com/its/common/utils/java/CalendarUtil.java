package com.its.common.utils.java;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
/**
 * 
 * @author tzz
 * @date 2019/02/22
 * @Introduce: Write describe here
 */
public class CalendarUtil {

	/** 获取年、月、日、时、分、秒、毫秒 */
	public static String getCurrYMDHMSM() {
	    // 子类实例化
		Calendar calendar = new GregorianCalendar();
		// 获取年、月、日、时、分、秒、毫秒
		int currYEAR = calendar.get(Calendar.YEAR);
		int currMONTH = calendar.get(Calendar.MONTH) + 1;
		int currDAY = calendar.get(Calendar.DAY_OF_MONTH);
		int currHOUR = calendar.get(Calendar.HOUR_OF_DAY);
		int currMINUTE = calendar.get(Calendar.MINUTE);
		int currSECOND = calendar.get(Calendar.SECOND);
		int currMILLISECOND = calendar.get(Calendar.MILLISECOND);
		System.out.println("年： " + currYEAR);
		System.out.println("月 " + currMONTH);
		System.out.println("日： " + currDAY);
		System.out.println("时： " + currHOUR);
		System.out.println("分： " + currMINUTE);
		System.out.println("秒： " + currSECOND);
		System.out.println("毫秒 " + currMILLISECOND);
		return currYEAR + "-" + currMONTH + "-" + currDAY + " " + currHOUR + ":" + currMINUTE + ":" + currSECOND + ":"
				+ currMILLISECOND;
	}

	/** 当前月第一天 */
	public static String getCurrMonthFirstDay() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date firstTime = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(firstTime) + " 00:00:00";
	}

	/** 当前月最后一天 */
	public static String getCurrMonthLastDay() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.DATE, 1);
		calendar.roll(Calendar.DATE, -1);
		Date lastTime = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(lastTime) + " 23:59:59";
	}

	/** 指定年月第一天 */
	public static String getMonthFirstDay(int year, int moth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, moth - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date firstTime = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(firstTime) + " 00:00:00";

	}

	/** 指定年月最后一天 */
	public static String getMonthLastDay(int year, int moth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, moth - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.DATE, 1);
		calendar.roll(Calendar.DATE, -1);
		Date lastTime = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(lastTime) + " 23:59:59";
	}

	/** 根据日期取得星期几*/
	public static String getWeekArr(Date date) {
		String[] weeks = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int weekIndex = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (weekIndex < 0) {
		    weekIndex = 0;
		}
		return weeks[weekIndex];
	}

	/** 根据日期取得星期几*/
	public static String getWeek(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINESE);
		return sdf.format(date);
	}

}
