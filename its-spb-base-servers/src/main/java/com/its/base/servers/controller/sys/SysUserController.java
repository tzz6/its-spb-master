package com.its.base.servers.controller.sys;

import com.its.base.servers.api.sys.domain.JobManager;
import com.its.base.servers.api.sys.domain.SysUser;
import com.its.base.servers.api.sys.domain.SysUserRole;
import com.its.base.servers.context.UserSession;
import com.its.base.servers.controller.verify.CaptchaConfiguration;
import com.its.base.servers.service.CaptchaService;
import com.its.base.servers.service.JobManagerService;
import com.its.base.servers.service.SysUserRoleService;
import com.its.base.servers.service.SysUserService;
import com.its.common.crypto.simple.Md5ShaCryptoUtil;
import com.its.common.dto.BaseResponse;
import com.its.common.enums.ResponseEnum;
import com.its.common.jwt.JwtUtil;
import com.its.common.model.Datagrid;
import com.its.common.redis.service.RedisService;
import com.its.common.utils.Constants;
import com.its.common.utils.IpUtil;
import com.its.common.utils.PrimaryKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 用户管理
 *
 * @author tzz
 */
@Controller
@RequestMapping(value = "/sysUser")
public class SysUserController {

    private static final Logger log = LoggerFactory.getLogger(SysUserController.class);

    private SysUserService sysUserService;
    private SysUserRoleService sysUserRoleService;
    private JobManagerService jobManagerService;
    private RedisService redisService;
    private CaptchaService captchaService;
    private CaptchaConfiguration captchaConfiguration;

    @Autowired
    public SysUserController(SysUserService sysUserService, SysUserRoleService sysUserRoleService, JobManagerService jobManagerService, RedisService redisService, CaptchaService captchaService, CaptchaConfiguration captchaConfiguration) {
        this.sysUserService = sysUserService;
        this.sysUserRoleService = sysUserRoleService;
        this.jobManagerService = jobManagerService;
        this.redisService = redisService;
        this.captchaService = captchaService;
        this.captchaConfiguration = captchaConfiguration;
    }

    /**
     * 用户管理列表数据
     *
     * @param stCode stCode
     * @param page   page
     * @param rows   rows
     * @return data
     */
    @GetMapping("/sysUserManage")
    public @ResponseBody Datagrid<SysUser> sysUserManage(
            @RequestParam(value = "stCode", required = false) String stCode,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "rows") Integer rows) {
        return getSysUserList(stCode, page, rows);
    }

    /**
     * 用户管理列表数据
     *
     * @param stCode stCode
     * @param page page
     * @param rows rows
     * @return Datagrid<SysUser>
     */
    @GetMapping("/sysUserManageApi")
    public @ResponseBody BaseResponse<Datagrid<SysUser>> sysUserManageApi(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "rows", defaultValue = "20") int rows,
            @RequestParam(value = "stCode", required = false) String stCode) {
        return new BaseResponse<Datagrid<SysUser>>().success(getSysUserList(stCode, page, rows));
    }

    private Datagrid<SysUser> getSysUserList(String stCode, Integer page, Integer rows) {
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
    public @ResponseBody
    BaseResponse<String> addSysUser(SysUser sysUser) {

        BaseResponse<String> response = new BaseResponse<String>().success();
        try {
            // 当前登录用户
            SysUser currSysUser = UserSession.getUser();
            sysUser.setStName(sysUser.getStName());
            // 判断用户工号重复性
            String stCode = sysUser.getStCode();
            List<SysUser> sysUsers = sysUserService.getSysUserListByStCode(stCode);
            if (sysUsers != null && sysUsers.size() > 0) {
                return new BaseResponse<String>().fail(Constants.IS_REPAEAT, Constants.IS_REPAEAT);
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
            response = new BaseResponse<String>().fail(ResponseEnum.FAIL);
        }
        return response;
    }

    /**
     * 查询对应ID的USER
     *
     * @return SysUser
     */
    @RequestMapping(value = "/getSysUserById", method = {RequestMethod.POST})
    public @ResponseBody
    SysUser getSysUserById(@RequestParam(value = "stId") String stId) {
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
    public @ResponseBody
    String updateSysUser(SysUser sysUser) {
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
    public @ResponseBody
    String deleteSysUser(@RequestParam(value = "stId") String stId) {
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
     * @param stId   stId
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

    /**
     * 登录
     *
     * @param username     用户名
     * @param password     密码
     * @param verifyCode   验证码
     * @param reqId        reqId
     * @param lang         语言
     * @return Map
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResponse<String> verifyLogin(@RequestParam("username") String username,
                                           @RequestParam("password") String password,
                                           @RequestParam(value = "verifyCode") String verifyCode,
                                           @RequestParam(value = "reqId") String reqId,
                                           @RequestParam(value = "lang", required = false) String lang) {
        BaseResponse<String> response = new BaseResponse<String>();
        try {
            log.info("username: {} verifyCode: {} ", username, verifyCode);
            log.debug("username: {} verifyCode: {} lang: {} ", username, verifyCode, lang);
            if (verifyCode != null && verifyCode(reqId, verifyCode)) {
                Map<String, Object> map = new HashMap<>(16);
                map.put("stCode", username);
                // SHA512加盐加密方式:密码+盐(盐可随机生成存储至数据库或使用用户名，当前使用简单方式即盐为用户名)
                map.put("stPassword", Md5ShaCryptoUtil.sha512Encrypt(password + username));
                SysUser sysUser = sysUserService.login(map);
                if (sysUser != null) {
                    sysUser.setLanguage(lang);
                    // 生成refreshToken
                    String refreshToken = PrimaryKeyUtil.getUuId();
                    // 生成JWT token
                    String token = JwtUtil.generateToken(username, lang);
                    //数据放入redis
                    redisService.hset(token, "token", token);
                    redisService.hset(token, "username", username);
                    redisService.hset(token, "lang", lang);
                    //设置redis token的有效时间
                    boolean expire = redisService.expire(token, JwtUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
                    response.success(token);
                    log.info("expire: {} expireTm: {} ", expire, redisService.getExpire(refreshToken));
                } else {
                    response.fail(ResponseEnum.AUTH_LOGIN_FAILED);
                }
            } else {
                response.fail(ResponseEnum.AUTH_VERIFYOGIN_FAILED);
            }
        } catch (Exception e) {
            log.error("login:" + e.getMessage(), e);
        }
        return response;
    }

    /**
     * 校验验证码
     * @param reqId 请求id
     * @param verifyCode 结果值
     * @return 校验通过返回true，否则返回false
     */
    private boolean verifyCode(String reqId, String verifyCode) {
        String captchaType = captchaConfiguration.getCaptchaType();
        try {
            String captchaTypeSlide = "slide";
            if (captchaTypeSlide.equals(captchaType)) {
                return captchaService.verify(reqId, verifyCode, true) == null;
            } else {
                return captchaService.verifyCode(reqId, verifyCode);
            }
        } catch (Exception e) {
            log.error("Request Id: {}, Result: {}", reqId, verifyCode, e);
            return false;
        }
    }
}
