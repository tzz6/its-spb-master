package com.its.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
/**
 * 获取IP
 * @author tzz
 */
public class IpUtil {

	public static String getIpAddr(HttpServletRequest request) {
		String ip = null;
		try {
		    String unknown = "unknown";
			ip = request.getHeader("X-Real-IP");
			if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
				ip = request.getHeader("X-Forwarded-For");
			}
			if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			String local = "127.0.0.1";
			String localStr = "0:0:0:0:0:0:0:1";
			if (local.equals(ip) || localStr.equals(ip)) {
				ip = InetAddress.getLocalHost().getHostAddress();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}

	/** 获取本机IP */
	public static String getLocalIp() {
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}
}
