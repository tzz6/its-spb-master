package com.its.web.controller.login;

import java.io.Serializable;

/**
 * Description: SsoClientVo
 * Company: tzz
 *
 * @Author: tzz
 * Date: 2020/4/21 13:09
 */
public class SsoClientVo implements Serializable {

    private String clientUrl;
    private String jsessionid;

    public SsoClientVo() {}

    public SsoClientVo(String clientUrl, String jsessionid) {
        this.clientUrl = clientUrl;
        this.jsessionid = jsessionid;
    }

    public String getClientUrl() {
        return clientUrl;
    }


    public String getJsessionid() {
        return jsessionid;
    }
}
