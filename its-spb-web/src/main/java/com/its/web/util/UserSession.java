package com.its.web.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.its.common.utils.Constants;
import com.its.model.mybatis.dao.domain.SysMenu;
import com.its.model.mybatis.dao.domain.SysUser;


/**
 * 当前登录用户Session操作
 * 
 *
 */
public class UserSession {

	/** 获取当前登录用户Session中的User **/
	public static SysUser getUser() {
		HttpServletRequest request = null;
		try {
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return (SysUser) request.getSession().getAttribute(Constants.SESSION_KEY.ITS_USER_SESSION);
	}

	/** 将当前登录用户User设置到Session中 **/
	public static void setUser(SysUser user) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		request.getSession().setAttribute(Constants.SESSION_KEY.ITS_USER_SESSION, user);
	}

	/** 从Session中删除当前登录用户 **/
	public static void removeUser() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		request.getSession().invalidate();
	}
	
	/** 获取当前登录用户Session中的时区 **/
	public static String getTimezone() {
		HttpServletRequest request = null;
		try {
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return (String) request.getSession().getAttribute(Constants.SESSION_KEY.SYS_TIME_ZONE);
	}

	/**将当前登录用户SysMenu设置到Session中*/
	public static void setSysMenu(List<SysMenu> userMenus) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		request.getSession().setAttribute(Constants.SESSION_KEY.ITS_USER_MENU_SESSION, userMenus);
		
	}
	
	/** 获取当前登录用户Session中的SysMenu **/
	@SuppressWarnings("unchecked")
	public static List<SysMenu> getSysMenu() {
		HttpServletRequest request = null;
		try {
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return (List<SysMenu>) request.getSession().getAttribute(Constants.SESSION_KEY.ITS_USER_MENU_SESSION);
	}
	
	/**登录后需要拦截的权限URL设置到Session中*/
	public static void setInterceptorSysMenu(List<SysMenu> userMenus) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		request.getSession().setAttribute(Constants.SESSION_KEY.ITS_INTERCEPTOR_USER_MENU_SESSION, userMenus);
		
	}
	
	/** 获取登录后需要拦截的权限URL **/
	@SuppressWarnings("unchecked")
	public static List<SysMenu> getInterceptorSysMenu() {
		HttpServletRequest request = null;
		try {
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return (List<SysMenu>) request.getSession().getAttribute(Constants.SESSION_KEY.ITS_INTERCEPTOR_USER_MENU_SESSION);
	}
}