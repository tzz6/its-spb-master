package com.its.base.servers.controller.sys;

import com.its.base.servers.service.SysReportService;
import com.its.common.model.Datagrid;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 报表管理
 * @author tzz
 */
@RestController
@RequestMapping(value = "/sysReport")
@Api("报表管理")
public class SysReportController {

	private static final Logger log = LoggerFactory.getLogger(SysReportController.class);

	private SysReportService sysReportService;

	@Autowired
    public SysReportController(SysReportService sysReportService) {
        this.sysReportService = sysReportService;
    }

    /** 存储报表SQL */
	private static Map<String, String> reportSqlMap;
	/** 页面动态条件SQL */
	private static Map<String, String> pageValueSqlMap;

	// 使用Map模拟数据库存储
	static {
		reportSqlMap = new HashMap<>();
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
		pageValueSqlMap = new HashMap<>();
		pageValueSqlMap.put("SYS_NAME", pageValueSql);
	}



	/**
     * 报表管理列表头
     *
     * @return Set
     */
    @GetMapping(value = "/tableHeart")
    public Set<String> index() {
        String regex = "<where[^>]*?>[\\s\\S]*?</where>";
        String exeSql = reportSqlMap.get("USER_REPORT_SQL").replaceAll(regex, "");
        List<Map<String, Object>> result = sysReportService.execute(exeSql, 0 + "", 1 + "");
        Map<String, Object> map = result.get(0);
        return map.keySet();
    }

	/**
	 * 报表管理列表数据
	 *
	 * @param request request
	 * @param page page
	 * @param rows rows
	 * @return Datagrid<Map<String, Object>>
	 */
	@ApiOperation("报表列表")
	@PostMapping("/sysReportManage")
	public Datagrid<Map<String, Object>> sysUserManage(HttpServletRequest request,
												@RequestParam(value = "page") Integer page, @RequestParam(value = "rows") Integer rows) {
		String exeSql = addWhereParam(request, reportSqlMap.get("USER_REPORT_SQL"));
		long total = sysReportService.getCount(exeSql);
		int startNum = (page - 1) * rows;
		log.info("exeSql:" + exeSql);
		log.info("startNum:" + startNum + ",rows:" + rows + "," + total);
		List<Map<String, Object>> result = sysReportService.execute(exeSql, startNum + "", rows + "");
		return new Datagrid<>(total, result);
	}

	/**
	 * 组装条件
	 *
	 * @param request request
	 * @param sql sql
	 * @return String
	 */
	private String addWhereParam(HttpServletRequest request, String sql) {
		String exeSql = sql;
		Enumeration<String> parameterNames = request.getParameterNames();
		try {
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
            log.error("SysReportController addWhereParam：" + e.getMessage(), e);
        }
		return exeSql;
	}

	/**
	 * 报表管理列表页面条件查询动态参数
	 *
	 * @param sqlKey sqlKey
	 * @return List<Map<String, Object>>
	 */
	@PostMapping("/sysReportPageValue")
	public @ResponseBody List<Map<String, Object>> sysReportPageValue(@RequestParam(value = "sqlKey") String sqlKey) {
		return sysReportService.execute(pageValueSqlMap.get(sqlKey));
	}

}
