package com.its.web.common.interceptor;

import com.its.common.jwt.JwtUtil;
import com.its.common.redis.service.RedisService;
import com.its.common.utils.Constants;
import com.its.model.mybatis.dao.domain.SysMenu;
import com.its.model.mybatis.dao.domain.SysUser;
import com.its.service.mybatis.SysMenuService;
import com.its.service.mybatis.SysUserService;
import com.its.web.controller.login.SsoClientVo;
import com.its.web.util.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 权限拦截器
 * @author tzz
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private RedisService redisService;

    @Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception ex)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView model)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 不需登录要过滤的url
		String[] notLoginFilterUrl = new String[] { "login", "logout", "webcam", "test" };
		// 登录后不需要过滤的url
		String[] loginNotFilterUrl = new String[] { "index", "css", "js", "images" };
		// 请求的servletPath
		String servletPath = request.getServletPath();
		logger.info("servletPath:" + servletPath);
		// 项目请求路径
		String contextPath = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath;
		logger.info("basePath:" + basePath);
		// 当前登录用户Session中的User
		SysUser currUser = UserSession.getUser();
		String token = UserSession.getToken();
		//全局Session token及cas.com局部Session currUser都存在
		if (currUser!=null && token != null) {
			String redirectUrl = request.getParameter("redirectUrl");
			//SSO服务端已完成单点登录,SSO客户端系统域名(如its.com/spb.com)直接跳转首页
			response.sendRedirect(redirectUrl + "?token=" + token);
		}
		// 需要过滤的URL
		if (checkFilter(servletPath, notLoginFilterUrl)) {
			if (null == currUser) {
				String reqToken = request.getParameter("token");
				//token不为空，如果token的Jwt和rddis校验有效则无需再登录
				if (ssoCheckLogin(basePath, reqToken)) {
					return true;
				} else {
					// 未登录
					response.sendRedirect("http://cas.com:8080/login?redirectUrl=" + basePath+"/index");
				}
			} else {
			    // 已登录,获取登录后需要拦截的权限URL
				List<SysMenu> interceptorSysMenus = UserSession.getInterceptorSysMenu();
				if (null == interceptorSysMenus) {
					Map<String, Object> maps = new HashMap<>(16);
					maps.put("sysNameCode", Constants.SYS_NAME_CODE);
					interceptorSysMenus = sysMenuService.getInterceptorUserMenus(maps);
					UserSession.setInterceptorSysMenu(interceptorSysMenus);
				}
				// 获取用户权限菜单
				getCurrMenus(currUser);
				if (checkFilter(servletPath, loginNotFilterUrl)) {
				    // 登录后需要拦截的URL
					if (checkFilter(servletPath, interceptorSysMenus)) {
					    // 当前登录用户所拥有权限的URL
						List<SysMenu> sysMenus = UserSession.getSysMenu();
						if (sysMenus!=null && checkFilter(servletPath, sysMenus)) {
							return true;
						} else {
							String method = request.getMethod();
							logger.info("method----------" + method);
							String get = "GET";
							if (get.equals(method)) {
								response.getWriter().write("<script>alert('没有权限');window.parent.location.href='"
										+ basePath + "/logout" + "'</script>");
							} else {
								response.getWriter().write(basePath + "/logout");
							}
							return false;
						}
					} else {
					    // 不需要权限控制的URL
						return true;
					}
				} else {
				    // 登录后不需要权限控制的URL
					return true;
				}
			}
		} else {
		    // 不需要过滤的URL
			return true;
		}
		return false;
	}

	/**
	 * 检查是否已通过sso单点登录认证
	 * @param basePath basePath
	 * @param reqToken reqToken
	 * @return  是否已通过sso单点登录认证
	 */
	private boolean ssoCheckLogin(String basePath, String reqToken) {
		if (reqToken != null && JwtUtil.verify(reqToken)) {
			SysUser currUser;// 从redis中获取单点登录认证信息
			Map<Object, Object> map = redisService.hmget(reqToken);
			if (map != null && map.size() > 0) {
				String username = map.get("username").toString();
				String lang = map.get("lang").toString();
				currUser = new SysUser();
				currUser.setStName(username);
				currUser.setStCode(username);
				currUser.setLanguage(lang);
				//将sysUser保存至Session
				String sessionId = UserSession.setUser(currUser);
				//记录SSO客户端请求url/logout，用于登出时触发SSO客户端登出
				String key = Constants.SessionKey.LOG_OUT + reqToken;
				SsoClientVo ssoClientVo = new SsoClientVo(basePath + "/logout", sessionId);
				redisService.lSet(key, ssoClientVo);
				redisService.expire(key, JwtUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/** 获取用户权限菜单 */
    private void getCurrMenus(SysUser currUser) {
        List<SysMenu> userMenus = UserSession.getSysMenu();
        if (null == userMenus) {
        	SysUser user = sysUserService.getSysUserByStId(currUser);
        	if (user != null) {
        		String lang = currUser.getLanguage();
        		Map<String, Object> maps = new HashMap<>(16);
        		maps.put("stId", user.getStId());
        		maps.put("sysNameCode", Constants.SYS_NAME_CODE);
        		maps.put("lang", lang);
        		userMenus = sysMenuService.getSysMenuListByUser(maps);
        		UserSession.setSysMenu(userMenus);
        	}
        }
    }

	/** 检查是否是要过滤的URL */
	private boolean checkFilter(String servletPath, List<SysMenu> interceptorSysMenus) {
		for (SysMenu menu : interceptorSysMenus) {
			String url = menu.getPermissionUrl();
			if (url != null && url.contains(servletPath)) {
				return true;
			}
		}
		return false;
	}

	/** 检查是否是要过滤的URL */
	private boolean checkFilter(String url, String[] notFilterUrl) {
		for (String str : notFilterUrl) {
			if (url.contains(str)) {
				return false;
			}
		}
		return true;
	}
}
