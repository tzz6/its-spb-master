package com.its.web.controller.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 菜单管理
 * 
 */
@Controller
@RequestMapping(value = "/sysMenu")
public class SysMenuController {

	private static final Log log = LogFactory.getLog(SysMenuController.class);

	/**
	 * 菜单管理列表页面
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/toSysMenuManage")
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		log.info("index");
		return "sysMenu/sysMenuManage";
	}

}
