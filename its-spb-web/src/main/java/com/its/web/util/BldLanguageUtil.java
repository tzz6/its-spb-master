package com.its.web.util;

import java.util.HashMap;
import java.util.Map;

public class BldLanguageUtil {

	public static Map<String, String> bldLanguageMaps = new HashMap<String, String>();
	
	private BldLanguageUtil() {//sonar修改，工具类不应该有公共的构造器，也就是说至少要有一个private的构造器，如果没有，默认的构造器是public的
		throw new IllegalStateException("Utility class");
	}

	public static Map<String, String> getBldLanguageMaps() {
		return bldLanguageMaps;
	}

	/**
	 * 获取语言值
	 * 
	 * @param key
	 *            语言CODE
	 * @param lang
	 *            语言
	 * @return
	 */
	public static String getValue(String key, String lang) {
		return bldLanguageMaps.get(key + "_" + lang);
	}

}
