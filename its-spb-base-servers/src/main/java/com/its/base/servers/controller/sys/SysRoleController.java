package com.its.base.servers.controller.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.its.base.servers.api.sys.domain.SysMenu;
import com.its.base.servers.api.sys.domain.SysName;
import com.its.base.servers.api.sys.domain.SysRole;
import com.its.base.servers.api.sys.domain.SysRoleMenu;
import com.its.base.servers.api.sys.domain.SysUser;
import com.its.base.servers.context.UserSession;
import com.its.base.servers.service.SysMenuService;
import com.its.base.servers.service.SysNameService;
import com.its.base.servers.service.SysRoleMenuService;
import com.its.base.servers.service.SysRoleService;
import com.its.base.servers.service.SysUserRoleService;
import com.its.common.model.Datagrid;
import com.its.common.utils.Constants;
import com.its.common.utils.PrimaryKeyUtil;


/**
 * 角色管理
 * @author tzz
 */
@Controller
@RequestMapping(value = "/sysRole")
public class SysRoleController {

	private static final Log log = LogFactory.getLog(SysRoleController.class);

	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysNameService sysNameService;
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	@RequestMapping(value = "/toSysRoleManage", method = RequestMethod.GET)
    public String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        return "sysRole/sysRoleManage";
    }

    /**
     * 
     * description: 角色管理列表数据
     * @author: tzz
     * date: 2019/08/26 20:02
     * @param request
     * @param roleName
     * @param sysNameCode
     * @param page
     * @param rows
     * @return Datagrid<SysRole>
     */
    @RequestMapping(value = "/sysRoleManage")
    public @ResponseBody Datagrid<SysRole> sysRoleManage(HttpServletRequest request,
            @RequestParam(value = "roleName", required = false) String roleName,
            @RequestParam(value = "sysNameCode", required = false) String sysNameCode,
            @RequestParam(value = "page") Integer page, @RequestParam(value = "rows") Integer rows) {
        // 当前登录用户
        SysUser currSysUser = UserSession.getUser();
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("roleName", roleName);
        map.put("sysNameCode", sysNameCode);
        map.put("lang", currSysUser.getLanguage());
        int total = sysRoleService.getSysRoleCount(map);
        int startNum = (page - 1) * rows;
        map.put("startNum", startNum);
        map.put("rows", rows);
        log.info("startNum:" + startNum + ",rows:" + rows + "," + total);
        List<SysRole> result = sysRoleService.getSysRoleList(map);
        Datagrid<SysRole> datagrid = new Datagrid<SysRole>(total, result);
        return datagrid;
    }

    /**
     * 系统名称列表
     * 
     * @return
     */
    @RequestMapping(value = "/getSysNameList")
    public @ResponseBody List<Map<String, String>> getSysNameList() {
        SysUser sysUser = UserSession.getUser();
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        List<SysName> sysNames = sysNameService.getSysNameByLang(sysUser.getLanguage());
        if (null == sysNames || sysNames.size() == 0) {
            return null;
        }
        for (SysName sn : sysNames) {
            Map<String, String> map = new HashMap<String, String>(16);
            map.put("sysNameCode", sn.getSysNameCode());
            map.put("name", sn.getName());
            result.add(map);
        }
        return result;
    }

    /**
     * 新增保存
     * 
     * @param request
     * @param sysRole
     * @return
     */
    @RequestMapping(value = "/addSysRole", method = RequestMethod.POST)
    public @ResponseBody String addSysRole(HttpServletRequest request, SysRole sysRole) {

        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        try {
            // 当前登录用户
            SysUser currSysUser = UserSession.getUser();
            sysRole.setRoleName(sysRole.getRoleName());
            sysRole.setRoleId(PrimaryKeyUtil.genPrimaryKey());
            Date currDate = new Date();
            sysRole.setCreateBy(currSysUser.getStCode());
            sysRole.setCreateTm(currDate);
            sysRole.setUpdateBy(currSysUser.getStCode());
            sysRole.setCreateTm(currDate);
            sysRoleService.insertSysRole(sysRole);
            log.info("新增角色成功---" + sysRole.getRoleName());
            return successFlag;
        } catch (Exception e) {
            log.error("新增角色失败：", e);
            e.printStackTrace();
            successFlag = Constants.OPTION_FLAG_FAIL;
            return successFlag;
        }
    }

    /**
     * 查询对应ID的Role
     * 
     * @param request
     * @param sysRole
     * @return
     */
    @RequestMapping(value = "/getSysRoleById")
    public @ResponseBody Map<String, Object> getSysUserById(HttpServletRequest request, SysRole sysRole) {
        sysRole = sysRoleService.getSysRoleById(sysRole);
        Map<String, Object> userMap = new HashMap<String, Object>(16);
        userMap.put("roleId", sysRole.getRoleId());
        userMap.put("roleName", sysRole.getRoleName());
        userMap.put("sysNameCode", sysRole.getSysNameCode());
        return userMap;
    }

    /**
     * 修改保存
     * 
     * @param request
     * @param sysRole
     * @return
     */
    @RequestMapping(value = "/updateSysRole", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody String updateSysRole(HttpServletRequest request, SysRole sysRole) {
        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        try {
            sysRole.setRoleName(sysRole.getRoleName());
            // 当前登录用户
            SysUser currSysUser = UserSession.getUser();
            Date currDate = new Date();
            sysRole.setUpdateBy(currSysUser.getStCode());
            sysRole.setCreateTm(currDate);
            sysRoleService.updateSysRole(sysRole);
            log.info("编辑角色成功---ID:" + sysRole.getRoleId());
            return successFlag;
        } catch (Exception e) {
            log.error("编辑角色失败：", e);
            e.printStackTrace();
            successFlag = Constants.OPTION_FLAG_FAIL;
            return successFlag;
        }
    }

    /**
     * 删除
     * 
     * @param request
     * @param response
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/deleteSysRole")
    public @ResponseBody String deleteSysRole(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "roleId") String roleId) {
        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        String[] roleIds = roleId.split(",");
        List<String> roleIdList = new ArrayList<String>();
        try {
            for (String id : roleIds) {
                sysUserRoleService.deleteSysUserRoleByRoleId(id);
                sysRoleMenuService.deleteSysRoleMenuByRoleId(id);
                roleIdList.add(id);
            }
            sysRoleService.deleteSysRole(roleIdList);
            log.info("删除角色成功---ID" + roleId);
        } catch (Exception e) {
            log.error("删除角色失败：", e);
            e.printStackTrace();
            successFlag = Constants.OPTION_FLAG_FAIL;
        }
        return successFlag;
    }

    /**
     * 系统菜单
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/getSysMenuList", method = RequestMethod.POST)
    public @ResponseBody Datagrid<SysMenu> getSysMenuList(HttpServletRequest request,
            @RequestParam(value = "menuName", required = false) String menuName,
            @RequestParam(value = "menuType", required = false) String menuType,
            @RequestParam(value = "sysNameCode", required = true) String sysNameCode,
            @RequestParam(value = "page") Integer page, @RequestParam(value = "rows") Integer rows) {
        // 当前登录用户
        SysUser currSysUser = UserSession.getUser();
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("menuName", menuName);
        map.put("menuType", menuType);
        map.put("sysNameCode", sysNameCode);
        map.put("lang", currSysUser.getLanguage());
        int total = sysMenuService.getSysMenuCount(map);
        int startNum = (page - 1) * rows;
        map.put("startNum", startNum);
        map.put("rows", rows);
        log.info("startNum:" + startNum + ",rows:" + rows + "," + total);
        List<SysMenu> result = sysMenuService.getSysMenuList(map);
        Datagrid<SysMenu> datagrid = new Datagrid<SysMenu>(total, result);
        return datagrid;
    }

    /**
     * 查询角色菜单列表
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getSysRoleMenuList")
    @ResponseBody
    public List<SysRoleMenu> getSysRoleMenuList(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "roleId", required = true) String roleId) {
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.getSysRoleMenuByRoleId(roleId);
        return sysRoleMenuList;
    }

    /**
     * 保存角色菜单关联
     * 
     * @param request
     * @param response
     * @param roleId
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/saveSysRoleMenu", method = RequestMethod.POST)
    public @ResponseBody String saveSysRoleMenu(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "roleId", required = true) String roleId,
            @RequestParam(value = "menuId", required = true) String menuId) {
        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        try {
            sysRoleMenuService.deleteSysRoleMenuByRoleId(roleId);
            if (menuId != null && !"".equals(menuId)) {
                String[] menuIds = menuId.split(",");
                List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
                for (String mId : menuIds) {
                    SysRoleMenu sysRoleMenu = new SysRoleMenu();
                    sysRoleMenu.setRoleId(roleId);
                    sysRoleMenu.setMenuId(mId);
                    list.add(sysRoleMenu);
                }
                sysRoleMenuService.saveSysRoleMenu(list);
            }
            log.info("保存角色菜单成功---roleId:" + roleId + "---menuId:" + menuId);
            return successFlag;
        } catch (Exception e) {
            log.error("保存角色菜单失败：", e);
            e.printStackTrace();
            successFlag = Constants.OPTION_FLAG_FAIL;
            return successFlag;
        }
    }

}
