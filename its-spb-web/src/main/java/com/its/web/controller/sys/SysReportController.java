package com.its.web.controller.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 报表管理
 * @author tzz
 */
@Controller
@RequestMapping(value = "/sysReport")
public class SysReportController {

	/**
	 * 报表管理列表页面
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/toSysReportManage")
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		return "sysReport/sysReportManage";
	}

}
