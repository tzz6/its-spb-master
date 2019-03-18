package com.its.gateway.domain;

import java.io.Serializable;

/**
 * Gateway动态路由
 * 
 * @author tzz
 */
public class GatewayRouter implements Serializable {

    private static final long serialVersionUID = 8172084302013491935L;

    /** id */
    private Integer id;
    /** key */
    private String key;
    /** 网关路由配置 */
    private String gatewayRoute;
    /** 状态（1：有效、0：无效） */
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGatewayRoute() {
        return gatewayRoute;
    }

    public void setGatewayRoute(String gatewayRoute) {
        this.gatewayRoute = gatewayRoute;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
