package com.its.web.common.interceptor;

import com.its.common.utils.Constants;
import com.its.model.mybatis.dao.domain.SysMenu;
import com.its.model.mybatis.dao.domain.SysUser;
import com.its.servers.facade.dubbo.sys.service.SysMenuFacade;
import com.its.servers.facade.dubbo.sys.service.SysUserFacade;
import com.its.web.util.UserSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

	private static final Logger logger = LogManager.getLogger(LoginInterceptor.class);

	@Autowired
	private SysUserFacade sysUserFacade;
	@Autowired
	private SysMenuFacade sysMenuFacade;

	public SysMenuFacade getSysMenuFacade() {
		return sysMenuFacade;
	}

	public void setSysMenuFacade(SysMenuFacade sysMenuFacade) {
		this.sysMenuFacade = sysMenuFacade;
	}

	public SysUserFacade getSysUserFacade() {
		return sysUserFacade;
	}

	public void setSysUserFacade(SysUserFacade sysUserFacade) {
		this.sysUserFacade = sysUserFacade;
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception ex)
			throws Exception {
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView model)
			throws Exception {
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		/** 不需登录要过滤的url */
		String[] notLoginFilterUrl = new String[] { "login", "logout", "webcam", "test" };
		/** 登录后不需要过滤的url */
		String[] loginNotFilterUrl = new String[] { "index", "css", "js", "images" };

		String servletPath = request.getServletPath();// 请求的servletPath
		logger.info("servletPath:" + servletPath);
		String contextPath = request.getContextPath();// 项目请求路径
		logger.info("contextPath:" + contextPath);
		String basePath = null;
		if (request.getServerPort() > 80) {
			basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ contextPath;
		} else {
			basePath = request.getScheme() + "://" + request.getServerName() + contextPath;
		}
		logger.info("basePath:" + basePath);
		SysUser currUser = UserSession.getUser();// 当前登录用户Session中的User
		if (checkFilter(servletPath, notLoginFilterUrl)) {// 需要过滤的URL
			if (null == currUser) {// 未登录
				response.sendRedirect(basePath + "/logout");
			} else {// 已登录
				// 获取登录后需要拦截的权限URL
				List<SysMenu> interceptorSysMenus = UserSession.getInterceptorSysMenu();
				if (null == interceptorSysMenus) {
					Map<String, Object> maps = new HashMap<String, Object>();
					maps.put("sysNameCode", Constants.SYS_NAME_CODE);
					interceptorSysMenus = sysMenuFacade.getInterceptorUserMenus(maps);
					UserSession.setInterceptorSysMenu(interceptorSysMenus);
				}
				// 获取用户权限菜单
				List<SysMenu> userMenus = UserSession.getSysMenu();
				if (null == userMenus) {
					SysUser user = sysUserFacade.getSysUserByStId(currUser);
					if (user != null) {
						String lang = currUser.getLanguage();
						Map<String, Object> maps = new HashMap<String, Object>();
						maps.put("stId", user.getStId());
						maps.put("sysNameCode", Constants.SYS_NAME_CODE);
						maps.put("lang", lang);
						userMenus = sysMenuFacade.getSysMenuListByUser(maps);
						UserSession.setSysMenu(userMenus);
					}
				}
				if (checkFilter(servletPath, loginNotFilterUrl)) {
					if (checkFilter(servletPath, interceptorSysMenus)) {// 登录后需要拦截的URL
						List<SysMenu> sysMenus = UserSession.getSysMenu();// 当前登录用户所拥有权限的URL
						if (checkFilter(servletPath, sysMenus)) {
							return true;
						} else {
							String method = request.getMethod();
							logger.info("method----------" + method);
							if (method.equals("GET")) {
								response.getWriter().write("<script>alert('没有权限');window.parent.location.href='"
										+ basePath + "/logout" + "'</script>");
							} else {
								response.getWriter().write(basePath + "/logout");
							}
							return false;
						}
					} else {// 不需要权限控制的URL
						return true;
					}
				} else {// 登录后不需要权限控制的URL
					return true;
				}
			}
		} else {// 不需要过滤的URL
			return true;
		}
		return false;
	}

	/** 检查是否是要过滤的URL */
	private boolean checkFilter(String servletPath, List<SysMenu> interceptorSysMenus) {
		for (SysMenu menu : interceptorSysMenus) {
			String url = menu.getPermissionUrl();
			if (url != null && url.indexOf(servletPath) != -1) {
				return true;
			}
		}
		return false;
	}

	/** 检查是否是要过滤的URL */
	protected boolean checkFilter(String url, String[] notFilterUrl) {
		for (String str : notFilterUrl) {
			if (url.indexOf(str) != -1) {
				return false;
			}
		}
		return true;
	}
}