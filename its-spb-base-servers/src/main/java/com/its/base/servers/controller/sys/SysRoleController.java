package com.its.base.servers.controller.sys;

import com.its.base.servers.api.sys.domain.*;
import com.its.base.servers.context.UserSession;
import com.its.base.servers.service.*;
import com.its.common.model.Datagrid;
import com.its.common.utils.Constants;
import com.its.common.utils.PrimaryKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * 角色管理
 * @author tzz
 */
@Controller
@RequestMapping(value = "/sysRole")
public class SysRoleController {

    private static final Logger log = LoggerFactory.getLogger(SysRoleController.class);
	private SysRoleService sysRoleService;
	private SysNameService sysNameService;
	private SysMenuService sysMenuService;
	private SysRoleMenuService sysRoleMenuService;
	private SysUserRoleService sysUserRoleService;

	@Autowired
    public SysRoleController(SysRoleService sysRoleService, SysNameService sysNameService, SysMenuService sysMenuService, SysRoleMenuService sysRoleMenuService, SysUserRoleService sysUserRoleService) {
        this.sysRoleService = sysRoleService;
        this.sysNameService = sysNameService;
        this.sysMenuService = sysMenuService;
        this.sysRoleMenuService = sysRoleMenuService;
        this.sysUserRoleService = sysUserRoleService;
    }

    @RequestMapping(value = "/toSysRoleManage", method = RequestMethod.GET)
    public String index(HttpServletResponse response, ModelMap modelMap) {
        return "sysRole/sysRoleManage";
    }

    /**
     *
     * description: 角色管理列表数据
     * @author: tzz
     * date: 2019/08/26 20:02
     * @param roleName roleName
     * @param sysNameCode sysNameCode
     * @param page page
     * @param rows rows
     * @return Datagrid<SysRole>
     */
    @RequestMapping(value = "/sysRoleManage")
    public @ResponseBody Datagrid<SysRole> sysRoleManage(
            @RequestParam(value = "roleName", required = false) String roleName,
            @RequestParam(value = "sysNameCode", required = false) String sysNameCode,
            @RequestParam(value = "page") Integer page, @RequestParam(value = "rows") Integer rows) {
        // 当前登录用户
        SysUser currSysUser = UserSession.getUser();
        Map<String, Object> map = new HashMap<>(16);
        map.put("roleName", roleName);
        map.put("sysNameCode", sysNameCode);
        if (currSysUser != null) {
            map.put("lang", currSysUser.getLanguage());
        }
        int total = sysRoleService.getSysRoleCount(map);
        int startNum = (page - 1) * rows;
        map.put("startNum", startNum);
        map.put("rows", rows);
        log.info("startNum:" + startNum + ",rows:" + rows + "," + total);
        List<SysRole> result = sysRoleService.getSysRoleList(map);
        return new Datagrid<>(total, result);
    }

    /**
     * 系统名称列表
     *
     * @return List<Map<String, String>>
     */
    @RequestMapping(value = "/getSysNameList")
    public @ResponseBody List<Map<String, String>> getSysNameList() {
        SysUser sysUser = UserSession.getUser();
        List<Map<String, String>> result = new ArrayList<>();
        if (sysUser != null) {
            List<SysName> sysNames = sysNameService.getSysNameByLang(sysUser.getLanguage());
            if (null == sysNames || sysNames.size() == 0) {
                return null;
            }
            for (SysName sn : sysNames) {
                Map<String, String> map = new HashMap<>(16);
                map.put("sysNameCode", sn.getSysNameCode());
                map.put("name", sn.getName());
                result.add(map);
            }
        }
        return result;
    }

    /**
     * 新增保存
     *
     * @param sysRole sysRole
     * @return String
     */
    @RequestMapping(value = "/addSysRole", method = RequestMethod.POST)
    public @ResponseBody String addSysRole(SysRole sysRole) {

        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        try {
            // 当前登录用户
            SysUser currSysUser = UserSession.getUser();
            sysRole.setRoleName(sysRole.getRoleName());
            sysRole.setRoleId(PrimaryKeyUtil.genPrimaryKey());
            Date currDate = new Date();
            if (currSysUser != null) {
                sysRole.setCreateBy(currSysUser.getStCode());
                sysRole.setUpdateBy(currSysUser.getStCode());
            }
            sysRole.setCreateTm(currDate);
            sysRole.setCreateTm(currDate);
            sysRoleService.insertSysRole(sysRole);
            log.info("新增角色成功---" + sysRole.getRoleName());
            return successFlag;
        } catch (Exception e) {
            log.error("新增角色失败：" + e.getMessage(), e);
            successFlag = Constants.OPTION_FLAG_FAIL;
            return successFlag;
        }
    }

    /**
     * 查询对应ID的Role
     *
     * @param sysRole sysRole
     * @return Map<String, Object>
     */
    @RequestMapping(value = "/getSysRoleById")
    public @ResponseBody Map<String, Object> getSysUserById(SysRole sysRole) {
        sysRole = sysRoleService.getSysRoleById(sysRole);
        Map<String, Object> userMap = new HashMap<>(16);
        userMap.put("roleId", sysRole.getRoleId());
        userMap.put("roleName", sysRole.getRoleName());
        userMap.put("sysNameCode", sysRole.getSysNameCode());
        return userMap;
    }

    /**
     * 修改保存
     *
     * @param sysRole sysRole
     * @return String
     */
    @RequestMapping(value = "/updateSysRole", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public @ResponseBody String updateSysRole(SysRole sysRole) {
        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        try {
            sysRole.setRoleName(sysRole.getRoleName());
            // 当前登录用户
            SysUser currSysUser = UserSession.getUser();
            Date currDate = new Date();
            if (currSysUser != null) {
                sysRole.setUpdateBy(currSysUser.getStCode());
            }
            sysRole.setCreateTm(currDate);
            sysRoleService.updateSysRole(sysRole);
            log.info("编辑角色成功---ID:" + sysRole.getRoleId());
            return successFlag;
        } catch (Exception e) {
            log.error("编辑角色失败：" + e.getMessage(), e);
            successFlag = Constants.OPTION_FLAG_FAIL;
            return successFlag;
        }
    }

    /**
     * 删除
     *
     * @param roleId roleId
     * @return String
     */
    @RequestMapping(value = "/deleteSysRole")
    public @ResponseBody String deleteSysRole(@RequestParam(value = "roleId") String roleId) {
        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        String[] roleIds = roleId.split(",");
        List<String> roleIdList = new ArrayList<>();
        try {
            for (String id : roleIds) {
                sysUserRoleService.deleteSysUserRoleByRoleId(id);
                sysRoleMenuService.deleteSysRoleMenuByRoleId(id);
                roleIdList.add(id);
            }
            sysRoleService.deleteSysRole(roleIdList);
            log.info("删除角色成功---ID" + roleId);
        } catch (Exception e) {
            log.error("删除角色失败：" + e.getMessage(), e);
            successFlag = Constants.OPTION_FLAG_FAIL;
        }
        return successFlag;
    }

    /**
     * 系统菜单
     *
     * @return Datagrid<SysMenu>
     */
    @RequestMapping(value = "/getSysMenuList", method = RequestMethod.POST)
    public @ResponseBody Datagrid<SysMenu> getSysMenuList(
            @RequestParam(value = "menuName", required = false) String menuName,
            @RequestParam(value = "menuType", required = false) String menuType,
            @RequestParam(value = "sysNameCode") String sysNameCode,
            @RequestParam(value = "page") Integer page, @RequestParam(value = "rows") Integer rows) {
        // 当前登录用户
        SysUser currSysUser = UserSession.getUser();
        Map<String, Object> map = new HashMap<>(16);
        map.put("menuName", menuName);
        map.put("menuType", menuType);
        map.put("sysNameCode", sysNameCode);
        if (currSysUser != null) {
            map.put("lang", currSysUser.getLanguage());
        }
        int total = sysMenuService.getSysMenuCount(map);
        int startNum = (page - 1) * rows;
        map.put("startNum", startNum);
        map.put("rows", rows);
        log.info("startNum:" + startNum + ",rows:" + rows + "," + total);
        List<SysMenu> result = sysMenuService.getSysMenuList(map);
        return new Datagrid<>(total, result);
    }

    /**
     * 查询角色菜单列表
     *
     * @return List<SysRoleMenu>
     */
    @RequestMapping(value = "/getSysRoleMenuList")
    @ResponseBody
    public List<SysRoleMenu> getSysRoleMenuList(@RequestParam(value = "roleId") String roleId) {
        return sysRoleMenuService.getSysRoleMenuByRoleId(roleId);
    }

    /**
     * 保存角色菜单关联
     *
     * @param roleId roleId
     * @param menuId menuId
     * @return String
     */
    @RequestMapping(value = "/saveSysRoleMenu", method = RequestMethod.POST)
    public @ResponseBody String saveSysRoleMenu(
            @RequestParam(value = "roleId") String roleId,
            @RequestParam(value = "menuId") String menuId) {
        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        try {
            sysRoleMenuService.deleteSysRoleMenuByRoleId(roleId);
            if (menuId != null && !"".equals(menuId)) {
                String[] menuIds = menuId.split(",");
                List<SysRoleMenu> list = new ArrayList<>();
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
            log.error("保存角色菜单失败：" + e.getMessage(), e);
            successFlag = Constants.OPTION_FLAG_FAIL;
            return successFlag;
        }
    }

}
