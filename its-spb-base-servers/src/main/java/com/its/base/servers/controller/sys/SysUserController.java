package com.its.base.servers.controller.sys;

import com.its.base.servers.api.sys.domain.JobManager;
import com.its.base.servers.api.sys.domain.SysUser;
import com.its.base.servers.api.sys.domain.SysUserRole;
import com.its.base.servers.context.UserSession;
import com.its.base.servers.service.JobManagerService;
import com.its.base.servers.service.SysUserRoleService;
import com.its.base.servers.service.SysUserService;
import com.its.common.crypto.simple.Md5ShaCryptoUtil;
import com.its.common.model.Datagrid;
import com.its.common.utils.Constants;
import com.its.common.utils.IpUtil;
import com.its.common.utils.PrimaryKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


/**
 * 用户管理
 * @author tzz
 */
@Controller
@RequestMapping(value = "/sysUser")
public class SysUserController {

    private static final Logger log = LoggerFactory.getLogger(SysUserController.class);

    private SysUserService sysUserService;
    private SysUserRoleService sysUserRoleService;
    private JobManagerService jobManagerService;

    @Autowired
    public SysUserController(SysUserService sysUserService, SysUserRoleService sysUserRoleService, JobManagerService jobManagerService) {
        this.sysUserService = sysUserService;
        this.sysUserRoleService = sysUserRoleService;
        this.jobManagerService = jobManagerService;
    }

    /**
     * 用户管理列表数据
     *
     * @param stCode stCode
     * @param page page
     * @param rows rows
     * @return Datagrid<SysUser>
     */
    @RequestMapping("/sysUserManage")
    public @ResponseBody Datagrid<SysUser> sysUserManage(
            @RequestParam(value = "stCode", required = false) String stCode, @RequestParam(value = "page") Integer page,
            @RequestParam(value = "rows") Integer rows) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("stCode", stCode);
        int total = sysUserService.getSysUserCount(map);
        int startNum = (page - 1) * rows;
        map.put("startNum", startNum);
        map.put("rows", rows);
        log.info("startNum:" + startNum + ",rows:" + rows + "," + total);
        List<SysUser> result = sysUserService.getSysUserList(map);
        return new Datagrid<>(total, result);
    }

    /**
     * 用户新增保存
     *
     * @param sysUser sysUser
     * @return String
     */
    @RequestMapping(value = "/addSysUser")
    public @ResponseBody String addSysUser(SysUser sysUser) {

        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        try {
            // 当前登录用户
            SysUser currSysUser = UserSession.getUser();
            sysUser.setStName(sysUser.getStName());
            // 判断用户工号重复性
            String stCode = sysUser.getStCode();
            List<SysUser> sysUsers = sysUserService.getSysUserListByStCode(stCode);
            if (sysUsers != null && sysUsers.size() > 0) {
                return Constants.IS_REPAEAT;
            }

            sysUser.setStId(PrimaryKeyUtil.genPrimaryKey());
            Date currDate = new Date();
            if (currSysUser != null) {
                sysUser.setCreateBy(currSysUser.getStCode());
                sysUser.setUpdateBy(currSysUser.getStCode());
            }
            sysUser.setCreateTm(currDate);
            sysUser.setCreateTm(currDate);
            String password = sysUser.getStCode();
            String salt = sysUser.getStCode();
            // SHA512加盐加密方式:密码+盐(盐可随机生成存储至数据库或使用用户名，当前使用简单方式即盐为用户名)
            String stPassword = Md5ShaCryptoUtil.sha512Encrypt(password + salt);
            sysUser.setStPassword(stPassword);
            sysUserService.insertSysUser(sysUser);
            log.info("新增用户成功---用户名为" + stCode);
            JobManager jobManager = new JobManager();
            jobManager.setServiceId(sysUser.getStId());
            jobManager.setServiceType("sys_user");
            jobManager.setStatus("0");
            jobManager.setIp(IpUtil.getLocalIp());
            jobManagerService.insertJobManager(jobManager);
        } catch (Exception e) {
            log.error("新增用户失败：" + e.getMessage(), e);
            successFlag = Constants.OPTION_FLAG_FAIL;
        }
        return successFlag;
    }

    /**
     * 查询对应ID的USER
     *
     * @return SysUser
     */
    @RequestMapping(value = "/getSysUserById", method = {RequestMethod.POST})
    public @ResponseBody SysUser getSysUserById(@RequestParam(value = "stId") String stId) {
        SysUser sysUser = new SysUser();
        sysUser.setStId(stId);
        return sysUserService.getSysUserByStId(sysUser);
    }

    /**
     * 用户修改保存
     *
     * @param sysUser sysUser
     * @return String
     */
    @RequestMapping(value = "/updateSysUser")
    public @ResponseBody String updateSysUser(SysUser sysUser) {
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
            List<SysUser> sysUsers = sysUserService.getSysUserListByStCode(stCode);
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
            if (currSysUser != null) {
                sysUser.setUpdateBy(currSysUser.getStCode());
            }
            sysUser.setUpdateTm(currDate);
            sysUserService.updateSysUser(sysUser);
            log.info("编辑用户成功---用户名为" + stCode);
        } catch (Exception e) {
            log.error("编辑用户失败：" + e.getMessage(), e);
            successFlag = Constants.OPTION_FLAG_FAIL;
        }
        return successFlag;
    }

    /**
     * 删除
     *
     * @param stId stId
     * @return String
     */
    @RequestMapping(value = "/deleteSysUser")
    public @ResponseBody String deleteSysUser(@RequestParam(value = "stId") String stId) {
        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        String[] stIds = stId.split(",");
        List<String> stIdList = new ArrayList<>();
        try {
            for (String id : stIds) {
                stIdList.add(id);
                sysUserRoleService.deleteSysUserRoleByStId(id);
            }
            sysUserService.deleteSysUser(stIdList);
            log.info("删除用户成功---用户ID" + stId);
        } catch (Exception e) {
            log.error("删除用户失败：" + e.getMessage(), e);
            successFlag = Constants.OPTION_FLAG_FAIL;
        }
        return successFlag;
    }

    /**
     * 查询用户角色列表
     *
     * @return List<SysUserRole>
     */
    @RequestMapping(value = "/getSysUserRoleList")
    @ResponseBody
    public List<SysUserRole> getSysUserRoleList(@RequestParam(value = "stId") String stId) {
        return sysUserRoleService.getSysUserRoleBystId(stId);
    }

    /**
     * 保存用户角色关联
     *
     * @param stId stId
     * @param roleId roleId
     * @return String
     */
    @RequestMapping(value = "/saveSysUserRole", method = RequestMethod.POST)
    public @ResponseBody
    String saveSysUserRole(@RequestParam(value = "stId") String stId,
                           @RequestParam(value = "roleId") String roleId) {
        String successFlag = Constants.OPTION_FLAG_SUCCESS;
        try {
            sysUserRoleService.deleteSysUserRoleByStId(stId);
            if (roleId != null && !"".equals(roleId)) {
                List<SysUserRole> list = new ArrayList<>();
                String[] roleIds = roleId.split(",");
                for (String rId : roleIds) {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setStId(stId);
                    sysUserRole.setRoleId(rId);
                    list.add(sysUserRole);
                }
                sysUserRoleService.saveSysUserRole(list);
            }
            log.info("保存用户角色成功---stId:" + stId + "---roleId:" + roleId);
            return successFlag;
        } catch (Exception e) {
            log.error("保存用户角色失败：" + e.getMessage(), e);
            successFlag = Constants.OPTION_FLAG_FAIL;
            return successFlag;
        }
    }
}
