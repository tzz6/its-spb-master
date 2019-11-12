package com.its.base.servers.context;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.its.base.servers.api.sys.domain.SysMenu;
import com.its.base.servers.api.sys.domain.SysUser;
import com.its.common.utils.Constants;


/**
 * 
 * description: 当前登录用户Session操作
 * company: tzz
 * @author: tzz
 * date: 2019/08/20 17:42
 */
public class UserSession {

    private static final Log log = LogFactory.getLog(UserSession.class);
    
	/** 获取当前登录用户Session中的User **/
	public static SysUser getUser() {
		HttpServletRequest request = null;
		SysUser sysUser = null;
		try {
		    request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		    sysUser = new SysUser();
		    String userName = request.getHeader("its-username");
		    String lang = request.getHeader("its-language");
		    sysUser.setStName(userName);
		    sysUser.setStCode(userName);
		    sysUser.setLanguage(lang);
		} catch (Exception e) {
            log.error("getUser：" + e.getMessage(), e);
			return null;
		}
		return sysUser;
	}

	/** 将当前登录用户User设置到Session中 **/
	public static void setUser(SysUser user) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
        request.getSession().setAttribute(Constants.SessionKey.ITS_USER_SESSION, user);
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
		    log.error("getTimezone：" + e.getMessage(), e);
			return null;
		}
		return (String) request.getSession().getAttribute(Constants.SessionKey.SYS_TIME_ZONE);
	}

	/**将当前登录用户SysMenu设置到Session中*/
	public static void setSysMenu(List<SysMenu> userMenus) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		request.getSession().setAttribute(Constants.SessionKey.ITS_USER_MENU_SESSION, userMenus);
		
	}
	
	/** 获取当前登录用户Session中的SysMenu **/
	@SuppressWarnings("unchecked")
	public static List<SysMenu> getSysMenu() {
		HttpServletRequest request = null;
		try {
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} catch (Exception e) {
		    log.error("getSysMenu：" + e.getMessage(), e);
			return null;
		}
		return (List<SysMenu>) request.getSession().getAttribute(Constants.SessionKey.ITS_USER_MENU_SESSION);
	}
	
	/**登录后需要拦截的权限URL设置到Session中*/
	public static void setInterceptorSysMenu(List<SysMenu> userMenus) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		request.getSession().setAttribute(Constants.SessionKey.ITS_INTERCEPTOR_USER_MENU_SESSION, userMenus);
		
	}
	
	/** 获取登录后需要拦截的权限URL **/
	@SuppressWarnings("unchecked")
	public static List<SysMenu> getInterceptorSysMenu() {
		HttpServletRequest request = null;
		try {
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} catch (Exception e) {
		    log.error("getInterceptorSysMenu：" + e.getMessage(), e);
			return null;
		}
		return (List<SysMenu>) request.getSession().getAttribute(Constants.SessionKey.ITS_INTERCEPTOR_USER_MENU_SESSION);
	}
}