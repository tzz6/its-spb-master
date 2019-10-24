package com.its.base.servers.controller.sys;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.its.base.servers.service.SysReportService;
import com.its.common.model.Datagrid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 报表管理
 * @author tzz
 */
@RestController
@RequestMapping(value = "/sysReport")
@Api("报表管理")
public class SysReportController {

	private static final Log log = LogFactory.getLog(SysReportController.class);

	@Autowired
	private SysReportService sysReportService;

	/** 存储报表SQL */
	private static Map<String, String> reportSqlMap = null;
	/** 页面动态条件SQL */
	private static Map<String, String> pageValueSqlMap = null;

	// 使用Map模拟数据库存储
	static {
		reportSqlMap = new HashMap<String, String>();
		String sql = "select u.ST_Code as '工号',u.ST_Name as '姓名',date_format(u.CREATE_TM,'%Y-%m-%d %H:%i:%S') as '创建时间', date_format(u.UPDATE_TM,'%Y-%m-%d %H:%i:%S') as '修改时间', "
				+ "r.ROLE_NAME as '角色名称',r.SYS_NAME_CODE as '系统名',m.MENU_NAME as '菜单名',m.MENU_URL as 'MENU_URL',m.MENU_TYPE as 'Menu_Type' "
				+ "from sys_user u " + "left join sys_user_role ur on u.ST_ID = ur.ST_ID "
				+ "left join (select * from sys_role sr where 1 = 1 <where>sr.sys_name_code</where>) r on ur.ROLE_ID = r.ROLE_ID "
				+ "left join sys_role_menu rm on r.ROLE_ID = rm.ROLE_ID "
				+ "left join sys_menu m on rm.MENU_ID = m.MENU_ID "
				+ "where 1=1 <where>u.st_code</where> <where>u.create_tm</where> <where>u.create_tm_to</where> "
				+ "order by u.CREATE_TM asc";

		reportSqlMap.put("USER_REPORT_SQL", sql);
		String pageValueSql = "select EN_NAME,BLD_NAME from sys_name sn left join bld_language bld on sn.BLD_CODE = bld.BLD_CODE WHERE LANG = 'zh'";
		pageValueSqlMap = new HashMap<String, String>();
		pageValueSqlMap.put("SYS_NAME", pageValueSql);
	}

	/**
     * 报表管理列表头
     * 
     * @param request
     * @param response
     * @param modelMap
     * @return
     */
    @GetMapping(value = "/tableHeart")
    public Set<String> index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        String regex = "<where[^>]*?>[\\s\\S]*?<\\/where>";
        String exeSql = reportSqlMap.get("USER_REPORT_SQL").replaceAll(regex, "");
        List<Map<String, Object>> result = sysReportService.execute(exeSql, 0 + "", 1 + "");
        Map<String, Object> map = result.get(0);
        Set<String> hearts = map.keySet();
        return hearts;
    }
	
	/**
	 * 报表管理列表数据
	 * 
	 * @param request
	 * @param page
	 * @param rows
	 * @return
	 */
	@ApiOperation("报表列表")
	@PostMapping("/sysReportManage")
	public Datagrid<Map<String, Object>> sysUserManage(HttpServletRequest request,
												@RequestParam(value = "page") Integer page, @RequestParam(value = "rows") Integer rows) {
		String exeSql = addWhereParam(request, reportSqlMap.get("USER_REPORT_SQL"));
		long total = sysReportService.getCount(exeSql);
		int startNum = (page - 1) * rows;
		log.info("startNum:" + startNum + ",rows:" + rows + "," + total);
		List<Map<String, Object>> result = sysReportService.execute(exeSql, startNum + "", rows + "");
		Datagrid<Map<String, Object>> datagrid = new Datagrid<Map<String, Object>>(total, result);
		return datagrid;
	}

	/**
	 * 组装条件
	 * 
	 * @param request
	 * @param sql
	 * @return
	 */
	public String addWhereParam(HttpServletRequest request, String sql) {
		String exeSql = sql;
		Enumeration<String> parameterNames = request.getParameterNames();
		StringBuilder stringBuilder = new StringBuilder();
		try {
			stringBuilder.append("");
			while (parameterNames.hasMoreElements()) {
				String name = parameterNames.nextElement();
				String paramValue = request.getParameter(name);
				String whereName = "<where>" + name + "</where>";
				if (StringUtils.isNotEmpty(paramValue)) {
					String whereValue = request.getParameter(name + "_where");
					if (StringUtils.isNotEmpty(whereValue)) {
					    // 将条件点位符表达式替换为真实条件
						whereValue = whereValue.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
						// 将参数点位符替换为真实条件数据
						whereValue = whereValue.replaceAll(whereName, paramValue);
						exeSql = exeSql.replaceAll(whereName, whereValue);
					}
				} else {// 查询条件无数据，则清除条件点位表达式
					exeSql = exeSql.replaceAll(whereName, "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exeSql;
	}

	/**
	 * 报表管理列表页面条件查询动态参数
	 * 
	 * @param request
	 * @param sqlKey
	 * @return
	 */
	@PostMapping("/sysReportPageValue")
	public @ResponseBody List<Map<String, Object>> sysReportPageValue(HttpServletRequest request,
			@RequestParam(value = "sqlKey") String sqlKey) {
		List<Map<String, Object>> result = sysReportService.execute(pageValueSqlMap.get(sqlKey));
		return result;
	}

}
