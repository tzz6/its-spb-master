package com.its.base.servers.service;

import com.its.base.servers.controller.verify.SlideCaptcha;
import com.its.base.servers.controller.verify.VerifyData;

import java.io.IOException;

/**
  * Description: 验证码提供程序
  * Company: 顺丰科技有限公司国际业务科技部
  * @Author: 01115486
  * Date: 2020/4/30 19:23
  */
public interface CaptchaService {

    /**
     * 生成验证码
     *
     * @param requestId 请求标识
     * @return 返回返回验证码数据
     * @throws IOException 读取原始图片异常或生成图片异常
     */
	SlideCaptcha create(String requestId) throws IOException;

    /**
     * 验证码校验
     *
     * @param requestId      请求标识
     * @param code           验证码
     * @param clearIfSuccess 验证通过，是否清除验证码
     * @return 验证结果，验证成功，返回null，验证失败，返回重新生成的验证码数据
     * @throws IOException 读取原始图片异常或生成图片异常
     */
	SlideCaptcha verify(String requestId, String code, Boolean clearIfSuccess) throws IOException;

    /**
     * 校验验证码
     * @param reqId 请求id
     * @param verifyCode 结果值
     * @return 校验通过返回true，否则返回false
     */
    boolean verifyCode(String reqId, String verifyCode);

    /**
     * <p>设置文字型验证码图片并存入redis缓存</p>
     * @author 89003389
     * @since 1.9
     * @param reqId 请求id
     * @return 验证码
     */
    VerifyData setPicCache(String reqId);

    /**
     * <p>从redis获取文字型验证码图片缓存</p>
     * @author 89003389
     * @since 1.9
     * @param reqId 请求id
     * @return 验证码
     */
    VerifyData getPicCache(String reqId);
}
