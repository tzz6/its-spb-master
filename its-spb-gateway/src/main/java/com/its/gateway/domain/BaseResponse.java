package com.its.gateway.domain;

import java.io.Serializable;

/**
 * Description: API接口基础返回信息
 * Company: tzz
 *
 * @Author: tzz
 * Date: 2020/4/3 14:13
 */
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 4437627868408059581L;
    /**
     * 状态码,0-成功,非0失败
     */
    private String code;
    /**
     * 描述信息
     */
    private String msg;
    /**
     * 具体返回的数据信息
     */
    private T data;

    public BaseResponse() {
    }

    public BaseResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResponse(ResponseEnum responseEnum) {
        this.fail(responseEnum.getCode(), responseEnum.getDesc());
    }

    public BaseResponse<T> success() {
        this.setCode(ResponseEnum.SUCCESS.getCode());
        this.setMsg(ResponseEnum.SUCCESS.getDesc());
        return this;
    }

    public BaseResponse<T> success(T data) {
        this.setCode(ResponseEnum.SUCCESS.getCode());
        this.setMsg(ResponseEnum.SUCCESS.getDesc());
        this.setData(data);
        return this;
    }

    public BaseResponse<T> fail(String code, String msg) {
        this.setCode(code);
        this.setMsg(msg);
        return this;
    }

    public BaseResponse<T> fail(ResponseEnum fail, String message) {
        return fail(fail.getCode(), message);
    }

    public BaseResponse<T> fail(ResponseEnum e) {
        this.setCode(e.getCode());
        this.setMsg(e.getDesc());
        return this;
    }

    public String getCode() {
        return code;
    }

    private void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    private void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    private void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" + "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
