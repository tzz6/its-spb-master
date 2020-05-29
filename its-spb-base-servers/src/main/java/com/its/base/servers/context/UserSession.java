package com.its.base.servers.context;

import com.its.base.servers.api.sys.domain.SysUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * description: 当前登录用户Session操作
 * company: tzz
 *
 * @author: tzz
 * date: 2019/08/20 17:42
 */
public class UserSession {

    private static final Log log = LogFactory.getLog(UserSession.class);

    /**
     * getHttpServletRequest
     **/
    private static HttpServletRequest getRequest() {
        HttpServletRequest request;
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                throw new NullPointerException();
            } else {
                request = ((ServletRequestAttributes) requestAttributes).getRequest();
            }
        } catch (Exception e) {
            log.error("HttpServletRequest:" + e.getMessage(), e);
            return null;
        }
        return request;
    }

    /**
     * 获取当前登录用户Session中的User
     **/
    public static SysUser getUser() {
        HttpServletRequest request = getRequest();
        try {
            if (request == null) {
                return null;
            } else {
                SysUser sysUser = new SysUser();
                String userName = request.getHeader("its-username");
                String lang = request.getHeader("its-language");
                sysUser.setStName(userName);
                sysUser.setStCode(userName);
                sysUser.setLanguage(lang);
                String cookie = request.getHeader("Cookie");
                log.info("cookie：" + cookie);
                return sysUser;
            }
        } catch (Exception e) {
            log.error("getUser：" + e.getMessage(), e);
            return null;
        }
    }
}
