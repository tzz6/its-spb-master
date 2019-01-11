package com.its.web.util;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class CookieUtil {

	public static String getCookie(HttpServletRequest request, String name) {
		String value = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (StringUtils.isNotEmpty(cookie.getName())) {
					if (cookie.getName().equals(name)) {
						value = cookie.getValue();
					}
				}
			}
		}
		return value;
	}

	public static void addCookie(HttpServletResponse response, String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(60*60);//单位秒
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public static void removeCookie(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}