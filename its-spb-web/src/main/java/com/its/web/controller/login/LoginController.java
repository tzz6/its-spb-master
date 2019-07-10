package com.its.web.controller.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.its.common.crypto.rsa.RsaCryptUtil;
import com.its.common.crypto.simple.Md5ShaCryptoUtil;
import com.its.common.utils.Constants;
import com.its.model.bean.MenuBean;
import com.its.model.mybatis.dao.domain.SysMenu;
import com.its.model.mybatis.dao.domain.SysUser;
import com.its.servers.facade.dubbo.sys.service.SysUserFacade;
import com.its.web.util.CookieUtil;
import com.its.web.util.UserSession;

/**
 * 
 * @author tzz
 * @工号: 
 * @date 2019/06/01
 * @Introduce: 登录
 */
@Controller
public class LoginController{

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private SysUserFacade sysUserFacade;

	/**
	   *  登录页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String toLogin(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String savePassword = CookieUtil.getCookie(request, Constants.CookieKey.SAVE_PASSWORD);
		String autoLogin = CookieUtil.getCookie(request, Constants.CookieKey.AUTO_LOGIN);
		String loginType = "1";
		if (autoLogin != null && loginType.equals(autoLogin)) {
			model.put("autoLogin", autoLogin);
		}
		if (savePassword != null && loginType.equals(savePassword)) {
			String username = CookieUtil.getCookie(request, Constants.CookieKey.USERNAME);
			String password = CookieUtil.getCookie(request, Constants.CookieKey.PASSWORD);
			model.put("savePassword", savePassword);
			model.put("username", username);
			model.put("password", password);
		}
		logger.info("to login page");
		return "login";
	}

	/**
	  *  登录
	 * @param username
	 * @param password
	 * @param verifyCode
	 * @param lang
	 * @param savePassword
	 * @param autologin
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, String> verifyLogin(@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam(value = "verifyCode", required = false) String verifyCode,
			@RequestParam(value = "lang", required = false) String lang,
			@RequestParam(value = "savePassword", required = false) String savePassword,
			@RequestParam(value = "autologin", required = false) String autologin, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> maps = new HashMap<>(16);
		try {
			String loginUrl = "/index";
            logger.info("username: {} verifyCode: {} ", username, verifyCode);
            logger.debug("username: {} verifyCode: {} lang: {} ", username, verifyCode, lang);
			String sessVerifyCode = (String) request.getSession().getAttribute(Constants.SessionKey.VERIFY_CODE);
			if (verifyCode != null && sessVerifyCode.equals(verifyCode.toUpperCase())) {
				Map<String, Object> map = new HashMap<String, Object>(16);
				map.put("stCode", username);
				//RAS私钥
				String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOBIIIvxHcTZw0A7fxOQGyTpz9Da"
						+ "0lc3CSq4Ya5j1+JZXI6Pub9PMa6rumsC7o+gC2KRcDxOAYwH9n8zFjPoF+iaUc8oCN+Okdd/gDmD"
						+ "RGwccRLE0dET1A/iKv8wnk/BVUmADWJvDx2QfNfsgPd/7dX9d2oEeK7IQvCLuBqjslrrAgMBAAEC"
						+ "gYAQvrHXYOglE1EVkZuaPU8ZgW9nm37KziwcCWoZmBC9MIjNiAOJOgNulBm19aEUDhHriQpFJlnN"
						+ "N6b6tji5JWHrcwgJk2R8WlG3kArerWLHIq5V93SDI/OQdHTBA6c2gIK2HAJ+ZgpDzh56Mq3p+erl"
						+ "Ud/fie/wmojn2cL6LeLWWQJBAP9FJOzjVYRIiae5RZ8g7k+xoclf1JKHlIcVZhENiGTWVdnO0eLI"
						+ "T2AEJja4FyjVsVVtE1Zo1Jh0zZGiRwQQq8UCQQDg7Ey2niY5eZntn8eq7fDuasEYMi1ztgqogap4"
						+ "3QAXG+DUM6kpno22IiQSmAceLV9e/fgzWOoekL7awTqYg+bvAkBTOsAnXJftYZlATnAcyifpZAlU"
						+ "FyLAA+SxhpCYzsjB2AB127EjOBxpOfEbtjoW3lXLfJzpd5SZgLvl1/s/oA/hAkBUAi5M7xjb0r1Z"
						+ "cZpED4czpY/ll6g+Vbn5YiTn67OC7hi1aW4/a0cGxg2vHDVcYhoDAtzXYNhg/jMqxY07NdjlAkEA"
						+ "gtTLxrw1WrQQ3Qj76l556ihm9xTYr/OYm+rq+oXmULmk/ud9MzEQ8mP0Pz/DmxV3KmU73JOrCfR3"
						+ "V9mrVTbe4Q==";
				byte[] decodedData = RsaCryptUtil.decryptByPrivateKey(RsaCryptUtil.decryptBASE64(password), privateKey);
				password = new String(decodedData);
				// SHA512加盐加密方式:密码+盐(盐可随机生成存储至数据库或使用用户名，当前使用简单方式即盐为用户名)
				map.put("stPassword", Md5ShaCryptoUtil.sha512Encrypt(password + username));
				SysUser sysUser = sysUserFacade.login(map);
				if (sysUser != null) {
					sysUser.setLanguage(lang);
					UserSession.setUser(sysUser);
					if (StringUtils.isNotBlank(savePassword)) {
						CookieUtil.addCookie(response, Constants.CookieKey.SAVE_PASSWORD, savePassword);
						CookieUtil.addCookie(response, Constants.CookieKey.USERNAME, username);
						CookieUtil.addCookie(response, Constants.CookieKey.PASSWORD, password);

						if (StringUtils.isNotBlank(autologin)) {
							CookieUtil.addCookie(response, Constants.CookieKey.AUTO_LOGIN, autologin);
						} else {
							CookieUtil.removeCookie(response, Constants.CookieKey.AUTO_LOGIN);
						}
					} else {
						CookieUtil.removeCookie(response, Constants.CookieKey.SAVE_PASSWORD);
						CookieUtil.removeCookie(response, Constants.CookieKey.USERNAME);
						CookieUtil.removeCookie(response, Constants.CookieKey.PASSWORD);
						CookieUtil.removeCookie(response, Constants.CookieKey.AUTO_LOGIN);
					}
                    maps.put("status", "success");
                    maps.put("message", loginUrl);

				} else {
                    maps.put("status", "userError");
                    maps.put("message", loginUrl);
				}
			} else {
                maps.put("status", "verifyCodeError");
                maps.put("message", loginUrl);
			}

		} catch (Exception e) {
			logger.error("login:" + e.getMessage(), e);
		}
		return maps;
	}

	/**
	 * 登出
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public void logout(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        logger.info("logout");
        UserSession.removeUser();
        try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            logger.error("logout:" + e.getMessage(), e);
        }
	}

	/**
	 * 首页
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws JsonProcessingException {
		logger.info("to index");
		String timezone = UserSession.getTimezone();
		logger.info("timezone:" + timezone);
		String menuJson = getMenu();
		modelMap.put("menuJson", menuJson);
		return "main";
	}

	/**
	 * 获取菜单
	 * 
	 * @return
	 * @throws JsonProcessingException
	 */
	public String getMenu() throws JsonProcessingException {
		List<SysMenu> userMenus = UserSession.getSysMenu();
		List<MenuBean> menuBeans = new ArrayList<MenuBean>();
		if (userMenus != null) {
			List<SysMenu> firstMenus = new ArrayList<SysMenu>();
			for (SysMenu sysMenu : userMenus) {
				if (null == sysMenu.getParentMenuId() && sysMenu.getMenuType() != null
						&& Constants.MenuType.MENU.equals(sysMenu.getMenuType())) {
					firstMenus.add(sysMenu);
				}
			}
			for (SysMenu firstMenu : firstMenus) {
				MenuBean menuBean = new MenuBean();
				String menuId = firstMenu.getMenuId().toString();
				menuBean.setIcon("icon-sys");
				menuBean.setMiHierarchicalstructure(menuId);
				menuBean.setUrl(firstMenu.getMenuUrl());
				menuBean.setMiParameter(menuId);
				menuBean.setMenuId(firstMenu.getMenuId());
				menuBean.setMenuname(firstMenu.getMenuName());

				List<SysMenu> twomenus = new ArrayList<SysMenu>();
				for (SysMenu menu : userMenus) {
					String parentMenuId = menu.getParentMenuId();
					if (parentMenuId != null && parentMenuId.equals(firstMenu.getMenuId()) && menu.getMenuType() != null
							&& Constants.MenuType.MENU.equals(menu.getMenuType())) {
						twomenus.add(menu);
					}
				}

				List<MenuBean> twoMenuBeans = new ArrayList<MenuBean>();
				for (SysMenu twoMenu : twomenus) {
					MenuBean twoMenuBean = new MenuBean();
					String twoMenuId = twoMenu.getMenuId();
					twoMenuBean.setIcon("icon-nav");
					twoMenuBean.setMiHierarchicalstructure(twoMenuId);
					twoMenuBean.setUrl(twoMenu.getMenuUrl());
					twoMenuBean.setMiParameter(menuBean.getMiHierarchicalstructure());
					twoMenuBean.setMenuId(twoMenuId);
					twoMenuBean.setMenuname(twoMenu.getMenuName());
					twoMenuBeans.add(twoMenuBean);
				}
				menuBean.setMenus(twoMenuBeans);
				menuBeans.add(menuBean);
			}
		}

		ObjectMapper objectMapper = new ObjectMapper();
		String menuJson = objectMapper.writeValueAsString(menuBeans);
		return menuJson;
	}

}
