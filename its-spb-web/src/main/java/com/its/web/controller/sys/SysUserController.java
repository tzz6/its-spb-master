package com.its.web.controller.sys;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.its.common.crypto.simple.Md5ShaCryptoUtil;
import com.its.common.utils.Constants;
import com.its.common.utils.PrimaryKeyUtil;
import com.its.model.bean.Datagrid;
import com.its.model.mybatis.dao.domain.JobManager;
import com.its.model.mybatis.dao.domain.SysUser;
import com.its.model.mybatis.dao.domain.SysUserRole;
import com.its.servers.facade.dubbo.sys.service.SysUserFacade;
import com.its.servers.facade.dubbo.sys.service.SysUserRoleFacade;
import com.its.service.mybatis.JobManagerService;
import com.its.web.util.IpUtil;
import com.its.web.util.UserSession;


/**
 * 用户管理
 * @author tzz
 */
@Controller
@RequestMapping(value = "/sysUser")
public class SysUserController {

    private static final Log log = LogFactory.getLog(SysUserController.class);

    @Autowired
    private SysUserFacade sysUserFacade;
    @Autowired
    private SysUserRoleFacade sysUserRoleFacade;

    @Autowired
    private JobManagerService jobManagerService;


    /**
     * 用户管理列表数据
     *
     * @param request
     * @param stCode
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/sysUserManage")
    public @ResponseBody
    Datagrid<SysUser> sysUserManage(HttpServletRequest request,
                                    @RequestParam(value = "stCode", required = false) String stCode, @RequestParam(value = "page") Integer page,
                                    @RequestParam(value = "rows") Integer rows) {
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("stCode", stCode);
        int total = sysUserFacade.getSysUserCount(map);
        int startNum = (page - 1) * rows;
        map.put("startNum", startNum);
        map.put("rows", rows);
        log.info("startNum:" + startNum + ",rows:" + rows + "," + total);
        List<SysUser> result = sysUserFacade.getSysUserList(map);
        Datagrid<SysUser> datagrid = new Datagrid<SysUser>(total, result);
        return datagrid;
    }

    /**
     * 用户新增保存
     *
     * @param request
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/addSysUser")
    public @ResponseBody
    String addSysUser(HttpServletRequest request, SysUser sysUser) {

        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        try {
            // 当前登录用户
            SysUser currSysUser = UserSession.getUser();
//			sysUser.setStName(URLDecoder.decode(sysUser.getStName(), "UTF-8"));
            sysUser.setStName(sysUser.getStName());
            // 判断用户工号重复性
            String stCode = sysUser.getStCode();
            List<SysUser> sysUsers = sysUserFacade.getSysUserListByStCode(stCode);
            if (sysUsers != null && sysUsers.size() > 0) {
                return Constants.IS_REPAEAT;
            }

            sysUser.setStId(PrimaryKeyUtil.genPrimaryKey());
            Date currDate = new Date();
            sysUser.setCreateBy(currSysUser.getStCode());
            sysUser.setCreateTm(currDate);
            sysUser.setUpdateBy(currSysUser.getStCode());
            sysUser.setCreateTm(currDate);
            String password = sysUser.getStCode();
            String salt = sysUser.getStCode();
            // SHA512加盐加密方式:密码+盐(盐可随机生成存储至数据库或使用用户名，当前使用简单方式即盐为用户名)
            String stPassword = Md5ShaCryptoUtil.sha512Encrypt(password + salt);
            sysUser.setStPassword(stPassword);
            sysUserFacade.insertSysUser(sysUser);
            log.info("新增用户成功---用户名为" + stCode);
            JobManager jobManager = new JobManager();
            jobManager.setServiceId(sysUser.getStId());
            jobManager.setServiceType("sys_user");
            jobManager.setStatus("0");
            jobManager.setIp(IpUtil.getLocalIp());
            jobManagerService.insertJobManager(jobManager);
            return successFlag;
        } catch (Exception e) {
            log.error("新增用户失败：", e);
            e.printStackTrace();
            successFlag = Constants.OPTION_FLAG_FAIL;
            return successFlag;
        }
    }

    /**
     * 查询对应ID的USER
     *
     * @return
     */
    @RequestMapping(value = "/getSysUserById", method = {RequestMethod.POST})
    public @ResponseBody
    SysUser getSysUserById(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "stId", required = true) String stId) {
        SysUser sysUser = new SysUser();
        sysUser.setStId(stId);
        sysUser = sysUserFacade.getSysUserByStId(sysUser);
        Map<String, Object> userMap = new HashMap<String, Object>(16);
        userMap.put("stId", sysUser.getStId());
        userMap.put("stCode", sysUser.getStCode());
        userMap.put("stName", sysUser.getStName());
        return sysUser;
    }

    /**
     * 用户修改保存
     *
     * @param request
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/updateSysUser")
    public @ResponseBody
    String updateSysUser(HttpServletRequest request, SysUser sysUser) {

        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        try {
            // 当前登录用户
            SysUser currSysUser = UserSession.getUser();
            String stName = sysUser.getStName();
            if (stName != null && !"".equals(stName)) {
                sysUser.setStName(stName);
            }
            // 判断用户工号重复性
            String stCode = sysUser.getStCode();
            String currStId = sysUser.getStId();
            List<SysUser> sysUsers = sysUserFacade.getSysUserListByStCode(stCode);
            if (sysUsers != null && sysUsers.size() > 0) {
                if (sysUsers.size() > 1) {
                    return Constants.IS_REPAEAT;
                } else {
                    if (null == currStId || !currStId.equals(sysUsers.get(0).getStId())) {
                        return Constants.IS_REPAEAT;
                    }
                }
            }

            Date currDate = new Date();
            sysUser.setUpdateBy(currSysUser.getStCode());
            sysUser.setUpdateTm(currDate);
            sysUserFacade.updateSysUser(sysUser);
            log.info("编辑用户成功---用户名为" + stCode);
            return successFlag;
        } catch (Exception e) {
            log.error("编辑用户失败：", e);
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
     * @param stId
     * @return
     */
    @RequestMapping(value = "/deleteSysUser")
    public @ResponseBody
    String deleteSysUser(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "stId") String stId) {
        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        String[] stIds = stId.split(",");
        List<String> stIdList = new ArrayList<String>();
        try {
            for (String id : stIds) {
                stIdList.add(id);
                sysUserRoleFacade.deleteSysUserRoleByStId(id);
            }
            sysUserFacade.deleteSysUser(stIdList);
            log.info("删除用户成功---用户ID" + stId);
        } catch (Exception e) {
            log.error("删除用户失败：", e);
            e.printStackTrace();
            successFlag = Constants.OPTION_FLAG_FAIL;
        }
        return successFlag;
    }

    /**
     * 查询用户角色列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getSysUserRoleList")
    @ResponseBody
    public List<SysUserRole> getSysUserRoleList(HttpServletRequest request, HttpServletResponse response,
                                                @RequestParam(value = "stId", required = true) String stId) {
        List<SysUserRole> sysRoleMenuList = sysUserRoleFacade.getSysUserRoleBystId(stId);
        return sysRoleMenuList;
    }

    /**
     * 保存用户角色关联
     *
     * @param request
     * @param response
     * @param stId
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/saveSysUserRole", method = RequestMethod.POST)
    public @ResponseBody
    String saveSysUserRole(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "stId", required = true) String stId,
                           @RequestParam(value = "roleId", required = true) String roleId) {
        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        try {
            sysUserRoleFacade.deleteSysUserRoleByStId(stId);
            if (roleId != null && !"".equals(roleId)) {
                List<SysUserRole> list = new ArrayList<SysUserRole>();
                String[] roleIds = roleId.split(",");
                for (String rId : roleIds) {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setStId(stId);
                    sysUserRole.setRoleId(rId);
                    list.add(sysUserRole);
                }
                sysUserRoleFacade.saveSysUserRole(list);
            }
            log.info("保存用户角色成功---stId:" + stId + "---roleId:" + roleId);
            return successFlag;
        } catch (Exception e) {
            log.error("保存用户角色失败：", e);
            e.printStackTrace();
            successFlag = Constants.OPTION_FLAG_FAIL;
            return successFlag;
        }
    }

    /**
     * 用户管理列表页面
     * 修改为SpringBoot方式addViewControllers实现
     * @param request
     * @param response
     * @param modelMap
     * @return

     @RequestMapping(value = "/toSysUserManage")
     public String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
     return "sysUser/sysUserManage";
     }*/
}
