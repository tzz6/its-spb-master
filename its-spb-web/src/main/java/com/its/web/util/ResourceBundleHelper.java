package com.its.web.util;

import java.util.Locale;
import java.util.ResourceBundle;
/**
 * 读取资源配置文件
 *
 */
public class ResourceBundleHelper {
	
	public static final String LANG_BASENAME = "com.its.resource.lang";

	/**
	 * 根据资源名称获取相应语言的资源
	 * 
	 * @param resName  资源名称
	 * @return 相应语言的资源内容
	 */
	public static String get(String language, String resName) {
		Locale locale = null;

		if (null == language) {
			return resName;
		}

		if (language.contains("_")) {
			String[] arr = language.split("_");
			locale = new Locale(arr[0], arr[1]);
		} else {// 其他语言
			locale = new Locale(language);
		}

		ResourceBundle resourceBundle = ResourceBundle.getBundle(LANG_BASENAME, locale);
		return resourceBundle.getString(resName);
	}
}
