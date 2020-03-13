package com.its.common.enums;

/**
 * Description: ResponseEnum
 * Company: tzz
 *
 * @Author: tzz
 * Date: 2019/12/17 20:07
 */
public enum ResponseEnum {
    /**
     * 成功
     */
    SUCCESS("0", "成功"),
    /**
     * 系统请求处理异常
     */
    FAIL("999", "系统请求处理异常"),
    /**
     * 网关系统异常
     */
    GATEWAY_SYS_EXCEPTION("1000", "网关系统异常"),
    /**
     * 请求时间戳异常
     */
    TIME_EXCEPTION("1001", "请求时间戳异常"),
    /**
     * 参数非空校验异常
     */
    NULL_EXCEPTION("1003", "参数非空校验异常"),
    /**
     * 参数校验失败
     */
    PARAM_CHECK_ERROR("1004", "参数校验失败"),
    /**
     * 访问外部系统异常
     */
    DATA_DEAL_EXCEPTION("2001", "访问外部系统异常"),
    /**
     * 访问外部系统超
     */
    DATA_DEAL_ERROR("2002", "访问外部系统超时"),
    /**
     * 系统请求处理超时
     */
    CHANNEL_EXCEPTION("3001", "系统请求处理超时"),
    /**
     * 数据权限异常,鉴权失败
     */
    AUTH_FAILED("4001", "数据权限异常,鉴权失败");

    private String code;
    private String desc;

    ResponseEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
