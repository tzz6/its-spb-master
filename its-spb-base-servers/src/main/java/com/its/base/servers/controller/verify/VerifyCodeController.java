package com.its.base.servers.controller.verify;

import com.google.common.base.Objects;
import com.its.base.servers.service.CaptchaService;
import com.its.common.dto.BaseResponse;
import com.its.common.enums.ResponseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 滑动验证码
 * Company: tzz
 *
 * @Author: tzz
 * Date: 2020/5/20 14:06
 */
@RequestMapping("/verifyCode")
@RestController
public class VerifyCodeController {

    private static final Logger log = LoggerFactory.getLogger(VerifyCodeController.class);
    private CaptchaService captchaService;

    private CaptchaConfiguration captchaConfiguration;

    public VerifyCodeController(CaptchaService captchaService, CaptchaConfiguration captchaConfiguration) {
        this.captchaService = captchaService;
        this.captchaConfiguration = captchaConfiguration;
    }

    /**
     * 滑动验证码类型
     */
    private static final String CAPTCHA_TYPE_SLIDE = "slide";

    /**
     * token=
     */
    private static final String TOKEN_VALUE = "token=";

    /**
     * 生成验证码
     */
    @GetMapping("generator")
    public BaseResponse<Map<String, String>> genVerifyCode(@RequestParam(value = "id") String reqId) {
        BaseResponse<Map<String, String>> res = new BaseResponse<>();
        try {
            if (reqId == null) {
                res.fail(ResponseEnum.PARAM_CHECK_ERROR);
            } else {
                //生成验证码
                VerifyData data = generatorSlideCode(reqId);
                Map<String, String> map = new HashMap<>(2);
                map.put("type", data.getType());
                map.put("pic", data.getCombined());
                map.put("chars", data.getChineseChars().toString());
                res.success(map);
            }
        } catch (Exception e) {
            log.error("reqId: {}", reqId, e);
            res.fail(ResponseEnum.FAIL.getCode(), e.getMessage());
        }
        return res;
    }

    /**
     * 生成验证码
     */
    private VerifyData generatorSlideCode(String reqId) {
        String captchaType = captchaConfiguration.getCaptchaType();
        VerifyData data = null;
        if (Objects.equal(captchaType, CAPTCHA_TYPE_SLIDE)) {
            try {
                //生成验证码
                SlideCaptcha slideCaptcha = captchaService.create(reqId);
                if (slideCaptcha != null) {
                    data = new VerifyData();
                    data.setCombined(slideCaptcha.getBackgroundImage());
                    data.setChineseChars(new StringBuilder(slideCaptcha.getSliderImage()));
                    data.setRealPointsOffset(new StringBuilder(slideCaptcha.getBackgroundWidth() + ""));
                }
            } catch (IOException e) {
                log.error("Request id: {}", reqId, e);
            }
        } else {
            data = captchaService.getPicCache(reqId);
            if (data == null) {
                data = captchaService.setPicCache(reqId);
            }
        }
        if (data != null) {
            data.setType(captchaType);
        }
        return data;
    }


}
