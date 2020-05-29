package com.its.web.controller.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.its.common.crypto.rsa.RsaCryptUtil;
import com.its.common.crypto.simple.Md5ShaCryptoUtil;
import com.its.common.jwt.JwtUtil;
import com.its.common.redis.service.RedisService;
import com.its.common.utils.Constants;
import com.its.common.utils.HttpUtil;
import com.its.common.utils.PrimaryKeyUtil;
import com.its.model.bean.MenuBean;
import com.its.model.mybatis.dao.domain.SysMenu;
import com.its.model.mybatis.dao.domain.SysUser;
import com.its.service.mybatis.SysUserService;
import com.its.web.util.CookieUtil;
import com.its.web.util.UserSession;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Description: Login
 * Company: tzz
 *
 * @Author: tzz
 * Date: 2019/06/01
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    //constructor方式：是强制注入，Spring 开发团队建议：在JavaBean中永远使用构造方法进行依赖注入。对于必须的依赖，永远使用断言来确认。
    //setter方式：是选择注入，为了可选的或者可变的依赖，在Spring 4.3及以后的版本中，setter上面的@Autowired 注解是可以不写的
    //field变量方式：通过反射直接注入到fields@Autowired就是通过这种方式 尽量避免使用直接在属性上注入 field injection is not recommended
    //field变量方式field injection的坏处
    //1、你不能使用属性注入的方式构建不可变对象（对象不能标为final）
    //2、你的类和依赖容器强耦合，不能再容器外使用
    //3、你的类不能绕过反射（例如单元测试的时候）进行实例化，必须通过依赖容器才能实例化
    //4、实际的依赖被隐藏在外面，不是在构造方法或者其它方法里面反射的。
    //constructor方式坏处
    //5、注入的对象特别多的时候，我们的构造器就会显得非常的冗余、不好看，非常影响美观和可读性，维护起来也较为困难,
    // 一个类经常会有超过10个的依赖。如果使用构造方法的方式注入的话，构造方法会有10个参数。
    // 但是如果使用属性注入的话就没有这样的限制。
    // 但是一个类有很多的依赖，是一个危险的标志，因为很有可能这个类完成了超过一件事，违背了单一职责原则。

    private SysUserService sysUserService;
    private RedisService redisService;

    @Autowired
    public LoginController(SysUserService sysUserService, RedisService redisService) {
        this.sysUserService = sysUserService;
        this.redisService = redisService;
    }

    /**
     * 登录页面
     *
     * @param request req
     * @param model   model
     * @return login
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String toLogin(HttpServletRequest request, ModelMap model,
                          @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        logger.info("redirectUrl:" + redirectUrl);
        model.put("redirectUrl", redirectUrl);
        String savePassword = CookieUtil.getCookie(request, Constants.CookieKey.SAVE_PASSWORD);
        String autoLogin = CookieUtil.getCookie(request, Constants.CookieKey.AUTO_LOGIN);
        String loginType = "1";
        if (loginType.equals(autoLogin)) {
            model.put("autoLogin", autoLogin);
        }
        if (loginType.equals(savePassword)) {
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
     * 登录
     *
     * @param username     用户名
     * @param password     密码
     * @param verifyCode   验证码
     * @param lang         语言
     * @param savePassword 记住密码
     * @param autoLogin    自动登录
     * @param request      request
     * @param response     response
     * @return Map
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, String> verifyLogin(@RequestParam("username") String username,
                                           @RequestParam("password") String password,
                                           @RequestParam(value = "verifyCode") String verifyCode,
                                           @RequestParam(value = "lang", required = false) String lang,
                                           @RequestParam(value = "savePassword", required = false) String savePassword,
                                           @RequestParam(value = "autoLogin", required = false) String autoLogin,
                                           @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                                           HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> maps = new HashMap<>(16);
        try {
            String loginUrl = "/index";
            logger.info("username: {} verifyCode: {} ", username, verifyCode);
            logger.debug("username: {} verifyCode: {} lang: {} ", username, verifyCode, lang);
            String sessVerifyCode = (String) request.getSession().getAttribute(Constants.SessionKey.VERIFY_CODE);
            if (verifyCode != null && sessVerifyCode.equals(verifyCode.toUpperCase())) {
                Map<String, Object> map = new HashMap<>(16);
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
                SysUser sysUser = sysUserService.login(map);
                if (sysUser != null) {
                    sysUser.setLanguage(lang);
                    //将sysUser保存至Session
                    UserSession.setUser(sysUser);
                    // 生成refreshToken
                    String refreshToken = PrimaryKeyUtil.getUuId();
                    // 生成JWT token
                    String token = JwtUtil.generateToken(username, lang);
                    UserSession.setToken(token);
                    //数据放入redis
                    redisService.hset(token, "token", token);
                    redisService.hset(token, "username", username);
                    redisService.hset(token, "lang", lang);
                    //设置redis token的有效时间
                    boolean expire = redisService.expire(token, JwtUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
                    setCookie(username, password, savePassword, autoLogin, response);
                    //设置Cookie
                    setTokenCookie(token, refreshToken, response);
                    maps.put("status", "success");
                    maps.put("refreshToken", refreshToken);
                    maps.put("token", token);
                    loginUrl = redirectUrl + "?token=" + token;
                    logger.info("expire: {} expireTm: {} loginUrl: {} ", expire, redisService.getExpire(refreshToken), loginUrl);
                } else {
                    maps.put("status", "userError");
                }
            } else {
                maps.put("status", "verifyCodeError");
            }
            maps.put("message", loginUrl);
        } catch (Exception e) {
            logger.error("login:" + e.getMessage(), e);
        }
        return maps;
    }

    private void setCookie(String username, String password, String savePassword,
                           String autoLogin, HttpServletResponse response) {
        if (StringUtils.isNotBlank(savePassword)) {
            CookieUtil.addCookie(response, Constants.CookieKey.SAVE_PASSWORD, savePassword);
            CookieUtil.addCookie(response, Constants.CookieKey.USERNAME, username);
            CookieUtil.addCookie(response, Constants.CookieKey.PASSWORD, password);
            if (StringUtils.isNotBlank(autoLogin)) {
                CookieUtil.addCookie(response, Constants.CookieKey.AUTO_LOGIN, autoLogin);
            } else {
                CookieUtil.removeCookie(response, Constants.CookieKey.AUTO_LOGIN);
            }
        } else {
            CookieUtil.removeCookie(response, Constants.CookieKey.SAVE_PASSWORD);
            CookieUtil.removeCookie(response, Constants.CookieKey.USERNAME);
            CookieUtil.removeCookie(response, Constants.CookieKey.PASSWORD);
            CookieUtil.removeCookie(response, Constants.CookieKey.AUTO_LOGIN);
        }
    }

    private void setTokenCookie(String token, String refreshToken, HttpServletResponse response) {
        CookieUtil.addCookie(response, Constants.CookieKey.ITS_TOKEN, token);
        CookieUtil.addCookie(response, Constants.CookieKey.REFRESHTOKEN, refreshToken);
    }

    /**
      * Description: CAS token knock
      * @Author: tzz
      * Date: 2020/4/18 20:39
      * Param:
      * Return:
      */
    @ResponseBody
    @RequestMapping(value = "/cas/knock", method = RequestMethod.GET)
    public Map<String, String> casKnock(HttpServletResponse response, @RequestParam("token") String token) {
        logger.info("cas knock:" + token);
        Map<String, String> maps = new HashMap<>(16);
        try {
            if (token != null && JwtUtil.verify(token)) {
                // 从redis中获取单点登录认证信息
                Map<Object, Object> map = redisService.hmget(token);
                if (map != null) {
                    maps.put("status", "success");
                    maps.put("refreshToken", token);
                    maps.put("token", token);
                } else {
                    maps.put("status", "error");
                }
            } else {
                maps.put("status", "error");
            }
        } catch (Exception e) {
            logger.error("logout:" + e.getMessage(), e);
        }
        return maps;
    }
    /**
     * 登出
     *
     * @param response response
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        logger.info(Constants.SessionKey.LOG_OUT);
        try {
            // 请求的servletPath
            String servletPath = request.getServletPath();
            logger.info("servletPath:" + servletPath);
            String token = UserSession.getToken();
            SysUser currUser = UserSession.getUser();
            logger.info("username:{}", currUser.getStCode());
            UserSession.removeUser();
            if (token != null) {
                //SSO客户端logout
                String key = Constants.SessionKey.LOG_OUT + token;
                long size = redisService.lGetListSize(key);
                List<Object> ssoClients = redisService.lGet(key, 0, size - 1);
                ssoClients.forEach(ssoClient -> {
                    try {
                        SsoClientVo clientVo = (SsoClientVo) ssoClient;
                        HttpUtil.sendByRequest(clientVo.getClientUrl(), "GET", clientVo.getJsessionid());
                    } catch (IOException e) {
                        logger.error("SessionListener:" + e.getMessage(), e);
                    }
                });
                //删除redis
                redisService.del(token);
                //删除SSO单点登录客户端logout url记录
                redisService.del(Constants.SessionKey.LOG_OUT + token);
            }
            CookieUtil.removeCookie(response, Constants.CookieKey.USERNAME);
            CookieUtil.removeCookie(response, Constants.CookieKey.ITS_TOKEN);
            CookieUtil.removeCookie(response, Constants.CookieKey.REFRESHTOKEN);
            if (redirectUrl != null && !"".equals(redirectUrl)) {
                response.sendRedirect("http://cas.com:8080/login?redirectUrl=" + redirectUrl + "/index");
            } else {
                response.sendRedirect("/login");
            }
        } catch (IOException e) {
            logger.error("logout:" + e.getMessage(), e);
        }
    }

    /**
     * 首页
     *
     * @param modelMap modelMap
     * @return String
     * @throws JsonProcessingException ex
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap modelMap)
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
     * @return 菜单
     * @throws JsonProcessingException ex
     */
    public String getMenu() throws JsonProcessingException {
        List<SysMenu> userMenus = UserSession.getSysMenu();
        List<MenuBean> menuBeans = new ArrayList<>();
        if (userMenus != null) {
            List<SysMenu> firstMenus = new ArrayList<>();
            for (SysMenu sysMenu : userMenus) {
                if (null == sysMenu.getParentMenuId() && sysMenu.getMenuType() != null
                        && Constants.MenuType.MENU.equals(sysMenu.getMenuType())) {
                    firstMenus.add(sysMenu);
                }
            }
            for (SysMenu firstMenu : firstMenus) {
                MenuBean menuBean = new MenuBean();
                String menuId = firstMenu.getMenuId();
                menuBean.setIcon("icon-sys");
                menuBean.setMiHierarchicalstructure(menuId);
                menuBean.setUrl(firstMenu.getMenuUrl());
                menuBean.setMiParameter(menuId);
                menuBean.setMenuId(firstMenu.getMenuId());
                menuBean.setMenuname(firstMenu.getMenuName());

                List<SysMenu> twomenus = new ArrayList<>();
                for (SysMenu menu : userMenus) {
                    String parentMenuId = menu.getParentMenuId();
                    if (parentMenuId != null && parentMenuId.equals(firstMenu.getMenuId()) && menu.getMenuType() != null
                            && Constants.MenuType.MENU.equals(menu.getMenuType())) {
                        twomenus.add(menu);
                    }
                }

                List<MenuBean> twoMenuBeans = new ArrayList<>();
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
        return objectMapper.writeValueAsString(menuBeans);
    }

}
