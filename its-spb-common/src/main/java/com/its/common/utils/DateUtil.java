package com.its.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {

	private final static String yyyyMMdd = "yyyyMMdd";
	private final static String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	private final static String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";

	public static String getDateyyyyMMdd() {
		SimpleDateFormat format = new SimpleDateFormat(yyyyMMdd);
		return format.format(new Date());
	}

	public static String getDateyyyyMMddHHmmss() {
		SimpleDateFormat format = new SimpleDateFormat(yyyyMMddHHmmss);
		return format.format(new Date());
	}

	public static String getDateyyyyMMddHHmmssSSS() {
		SimpleDateFormat format = new SimpleDateFormat(yyyyMMddHHmmssSSS);
		return format.format(new Date());
	}

	public static String getCurrDate(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	public static void main(String[] args) throws ParseException {

		 Calendar cd = Calendar.getInstance();
		 
//		 String [] ids = TimeZone.getAvailableIDs();
//		 for (String id : ids) {
//			System.out.println(id);
//		}
		 System.out.println("当前时区："+cd.getTimeZone().getID());
//		 SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 //格林尼治时区
		 TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
		 cd.setTimeZone(gmtTimeZone);
		 System.out.println("当前时区："+cd.getTimeZone().getID());
		 System.out.println("格林尼治时间："+cd.getTime());
		 
		 TimeZone currTimeZone = TimeZone.getTimeZone("Asia/Bangkok");
		 sdf.setTimeZone(currTimeZone);
		 System.out.println("对应时区时间："+sdf.format(cd.getTime()));
		


	}
	
	public void test1() throws ParseException{

		SimpleDateFormat foo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("foo:" + foo.format(new Date()));

		Calendar gc = GregorianCalendar.getInstance();
		System.out.println("gc.getTime():" + gc.getTime());
		System.out.println("gc.getTimeInMillis():" + new Date(gc.getTimeInMillis()));

		// 当前系统默认时区的时间：
		Calendar calendar = new GregorianCalendar();
		System.out.print("时区：" + calendar.getTimeZone().getID() + "  ");
		System.out.println("时间：" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
		// 美国洛杉矶时区
		TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
		// 时区转换
		calendar.setTimeZone(tz);
		System.out.print("时区：" + calendar.getTimeZone().getID() + "  ");
		System.out.println("时间：" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

		// 1、取得本地时间：
		java.util.Calendar cal = java.util.Calendar.getInstance();

		// 2、取得时间偏移量：
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);

		// 3、取得夏令时差：
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

		// 之后调用cal.get(int x)或cal.getTimeInMillis()方法所取得的时间即是UTC标准时间。
		System.out.println("UTC:" + new Date(cal.getTimeInMillis()));

		Calendar calendar1 = Calendar.getInstance();
		TimeZone tztz = TimeZone.getTimeZone("GMT");
		calendar1.setTimeZone(tztz);
		System.out.println(calendar.getTime());
		System.out.println(calendar.getTimeInMillis());

		// SimpleDateFormat df = new
		// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		// df.setTimeZone(TimeZone.getTimeZone("UTC"));
		// System.out.println(df.parse("2014-08-23T09:20:05Z").toString());

		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Date t = new Date();
		System.out.println(df1.format(t));
		System.out.println(df1.format(df1.parse("2014-08-27T18:02:59.676Z")) + "***********");
		df1.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.println(df1.format(t));
		System.out.println("-----------");
		System.out.println(df1.format(df1.parse("2014-08-27T18:02:59.676Z")) + "***********");
		System.out.println("2014-08-27T18:02:59.676Z");
	}

}
