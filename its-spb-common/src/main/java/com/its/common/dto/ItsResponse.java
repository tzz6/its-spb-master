package com.its.common.dto;


import com.its.common.enums.ResponseEnum;

import java.io.Serializable;

/**
 * Description: ItsResponse
 * Company: tzz
 *
 * @author: tzz
 * Date: 2019/12/17 20:06
 */
public class ItsResponse<T> implements Serializable {
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

    public ItsResponse() {
    }

    public ItsResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ItsResponse(ResponseEnum responseEnum) {
        this.fail(responseEnum.getCode(), responseEnum.getDesc());
    }

    public ItsResponse<T> success() {
        this.setCode(ResponseEnum.SUCCESS.getCode());
        this.setMsg(ResponseEnum.SUCCESS.getDesc());
        return this;
    }

    public ItsResponse<T> success(T data) {
        this.setCode(ResponseEnum.SUCCESS.getCode());
        this.setMsg(ResponseEnum.SUCCESS.getDesc());
        this.setData(data);
        return this;
    }

    public ItsResponse<T> fail(String code, String msg) {
        this.setCode(code);
        this.setMsg(msg);
        return this;
    }

    public ItsResponse<T> fail(ResponseEnum fail, String message) {
        return fail(fail.getCode(), message);
    }

    public ItsResponse<T> fail(ResponseEnum e) {
        this.setCode(e.getCode());
        this.setMsg(e.getDesc());
        return this;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ItsResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
