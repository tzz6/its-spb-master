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
     * 网关系统异常
     */
    GATEWAY_SYS_EXCEPTION("09020000", "网关内部异常"),
    /**
     * 账号信息异常，请重新登录
     */
    ACCOUNT_NO_TOKEN("09020101", "访问账号信息异常，请重新登录，需要登录认证接口时在请求头中没有设置token,401Unauthorized"),
    /**
     * 账号异地登录，请重新登录
     */
    ACCOUNT_OTHER_LOGIN("09020102", "账号异地登录，请重新登录，网关根据header中设置的token找不到用户信息"),
    /**
     * IP非法
     */
    IP_BLACK_LIST("09020801", "IpBlackList:IP非法"),
    /**
     * 09020103	登录时的ticket参数是非法的
     * 09020201	禁止该APP访问
     * 09020202	内部的API，禁止所有的APP访问
     * 09020301	请求太过频繁
     * 09020401	网关根据header中设置的session-id找不到密钥
     * 09020402	没有在header中设置session-id
     * 09020403	网关使用session-id关联的密钥不能解密请求体
     * 09020501	网关没有该API的路由信息
     * 09020502	后台的微服务响应超时，默认20s
     * 09020601	登录时没有deviceid参数
     * 09020701	登录时的请求体不是一个合法的json
     */
    SUCCESS("0", "成功"),
    /**
     * 系统请求处理异常
     */
    FAIL("500", "系统请求处理异常"),
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
     * 4001 数据权限异常,鉴权失败
     */
    AUTH_FAILED("4001", "数据权限异常,鉴权失败"),
    /**
     * 4002 登录重试次数过多异常,鉴权失败
     */
    AUTH_RETRY_FAILED("4002", "登录重试次数过多异常,鉴权失败"),
    /**
     * 5001 登录失败.用户名或者密码错误
     */
    AUTH_LOGIN_FAILED("5001", "登录失败.用户名或者密码错误"),
    /**
     * 5002 验证码错误
     */
    AUTH_VERIFYOGIN_FAILED("5002", "验证码错误"),
    /**
     * 5003 会话超时过期
     */
    AUTH_SESSION_TIMEOUT("5003", "会话超时过期");

    private String code;
    private String desc;

    ResponseEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
