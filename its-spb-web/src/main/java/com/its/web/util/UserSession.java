
package com.its.web.util;

import com.its.common.utils.Constants;
import com.its.model.mybatis.dao.domain.SysMenu;
import com.its.model.mybatis.dao.domain.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 当前登录用户Session操作
 *
 * @author tzz
 */
public class UserSession {
    private static final Logger logger = LoggerFactory.getLogger(UserSession.class);

    /**
     * getHttpServletRequest
     **/
    public static HttpServletRequest getRequest() {
        HttpServletRequest request;
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                throw new NullPointerException();
            } else {
                request = ((ServletRequestAttributes) requestAttributes).getRequest();
            }
        } catch (Exception e) {
            logger.error("login:" + e.getMessage(), e);
            return null;
        }
        return request;
    }

    /**
     * 获取当前登录用户Session中的User
     **/
    public static SysUser getUser() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        } else {
            logger.info("SessionId:" + request.getSession().getId());
            return (SysUser) request.getSession().getAttribute(Constants.SessionKey.ITS_USER_SESSION);
        }
    }

    /**
     * 将当前登录用户User设置到Session中
     **/
    public static String setUser(SysUser user) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            String sessionId = request.getSession().getId();
            logger.info("SessionId:" + sessionId);
            request.getSession().setAttribute(Constants.SessionKey.ITS_USER_SESSION, user);
            return sessionId;
        } else {
            return null;
        }
    }

    /**
     * 将当前token设置到Session中
     **/
    public static void setToken(String token) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            logger.info("SessionId:" + request.getSession().getId());
            request.getSession().setAttribute(Constants.SessionKey.ITS_TOKEN, token);
        }
    }

    /**
     * 获取当前登录用户Session中的token
     **/
    public static String getToken() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        } else {
            return (String) request.getSession().getAttribute(Constants.SessionKey.ITS_TOKEN);
        }
    }

    /**
     * 从Session中删除当前登录用户
     **/
    public static void removeUser() {
        HttpServletRequest request = getRequest();
        if(request != null){
            logger.info("SessionId:" + request.getSession().getId());
			request.getSession().invalidate();
		}
    }

    /**
     * 获取当前登录用户Session中的时区
     **/
    public static String getTimezone() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			return null;
		} else {
			return (String) request.getSession().getAttribute(Constants.SessionKey.SYS_TIME_ZONE);
		}
    }

    /**
     * 将当前登录用户SysMenu设置到Session中
     */
    public static void setSysMenu(List<SysMenu> userMenus) {
        HttpServletRequest request = getRequest();
		if (request != null) {
			request.getSession().setAttribute(Constants.SessionKey.ITS_USER_MENU_SESSION, userMenus);
		}
    }

    /**
     * 获取当前登录用户Session中的SysMenu
     **/
    public static List<SysMenu> getSysMenu() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			return null;
		} else {
			return (List<SysMenu>) request.getSession().getAttribute(Constants.SessionKey.ITS_USER_MENU_SESSION);
		}
    }

    /**
     * 登录后需要拦截的权限URL设置到Session中
     */
    public static void setInterceptorSysMenu(List<SysMenu> userMenus) {
		HttpServletRequest request = getRequest();
		if (request != null) {
			request.getSession().setAttribute(Constants.SessionKey.ITS_INTERCEPTOR_USER_MENU_SESSION, userMenus);
		}
    }

    /**
     * 获取登录后需要拦截的权限URL
     **/
    @SuppressWarnings("unchecked")
    public static List<SysMenu> getInterceptorSysMenu() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			return null;
		} else {
			return (List<SysMenu>) request.getSession().getAttribute(Constants.SessionKey.ITS_INTERCEPTOR_USER_MENU_SESSION);
		}
    }

    /**
     * 获取当前请求中的refreshToken
     **/
    public static String getRefreshToken() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			return null;
		} else {
			String refreshToken = null;
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (Constants.CookieKey.REFRESHTOKEN.equals(cookie.getName())) {
					refreshToken = cookie.getValue();
				}
			}
			String cookie = request.getHeader("Cookie");
			logger.info("cookie:" + cookie);
			return refreshToken;
		}
    }
}
