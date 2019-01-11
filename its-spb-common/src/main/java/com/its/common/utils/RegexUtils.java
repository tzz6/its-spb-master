package com.its.common.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class RegexUtils {

	/**
	 * 判断是否正整数
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isNumber(String number) {
		if (null == number || "".equals(number))
			return true;
		String regex = "[0-9]*";
		return number.matches(regex);
	}

	/**
	 * 判断是否是正确的邮箱地址
	 * 
	 * @param email
	 * @return boolean true,通过，false，没通过
	 */
	public static boolean isEmail(String email) {
		if (null == email || "".equals(email))
			return true;
		String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		return email.matches(regex);
	}

	/**
	 * 判断是否含有中文，仅适合中国汉字，不包括标点
	 * 
	 * @param text
	 * @return boolean true,通过，false，没通过
	 */
	public static boolean isChinese(String text) {
		if (null == text || "".equals(text))
			return true;

		return true;// StringUtil.isChinese(text);
	}

	/**
	 * 判断几位小数(正数)
	 * 
	 * @param decimal
	 *            数字
	 * @param count
	 *            小数位数
	 * @return boolean true,通过，false，没通过
	 */
	public static boolean isDecimal(String decimal, int count) {
		String regex = "^(-)?(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){" + count + "})?$";
		return decimal.matches(regex);
	}

	@SuppressWarnings("unused")
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	/*
	 * 格式 yyyy-MM-dd hh:mm:ss *
	 */
	public static boolean isDate(String date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setLenient(false);// 设置为false，否则平年的2月29号也会为true
		try {
			format.parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	// 英文或者数字
	public static boolean isEnglish(String string) {
		return string.matches("^[A-Za-z,\\x21-\\x7e,\\s]+$");
	}

	// 字母和数字
	public static boolean isNumAndLetter(String string) {

		return string.matches("^[a-zA-Z0-9]+$");
	}

	// 阿拉伯数字和空格或中横框(其他国家)
	public static boolean isPost(String string) {

		return string.matches("^(?:\\d|\\-|\\s)+$");
	}

	// 英文字母和数字或空格(英国和加拿大)
	public static boolean isGBOrCA(String string) {

		return string.matches("^[a-zA-Z0-9 ]*$");
	}
	//字母  数字 中横杠
	public static boolean isNumberOrCharacter(String string) {

		return string.matches("^[a-zA-Z0-9-]+$");
	}
	//字母  数字 中横杠
	public static boolean isNumberOrCharacterOrSpace(String string) {

		return string.matches("^[a-zA-Z0-9-\\s]+$");
	}
	/**
	 * 
	 * @Title: getChinaType @Description:
	 *         TODO(用于校验国家代码为CN、HK、MO、TW时，城市可以输入中文和英文，其它国家时只能输入英文字符,把枚举类型变量转成字符串) @return
	 *         list 返回类型 @throws
	 */

	public static List<String> getChinaType() {
		List<String> returnChinaTypes = new ArrayList<String>();
		return returnChinaTypes;
	}

	/**
	 * 
	 * @Title: getGBAndCA @Description:
	 *         TODO(用于校验英国和加拿大的邮编由英文字母和数字或空格组成，其它国家邮编由阿拉伯数字和空格或中横框组成) @return
	 *         list 返回类型 @throws
	 */

	public static List<String> getGBAndCA() {
		List<String> returnPostTypes = new ArrayList<String>();
		return returnPostTypes;
	}
}
