package com.its.base.servers.controller.verify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 验证码配置
 *
 * @author 01389232
 *
 */
@Service
public class CaptchaConfiguration {
	@Value("${captcha.type:slide}")
	private String  captchaType;

	@Value("${captcha.slide.image.path:/static/img/captcha/}")
	private String  captchaSlideImagePath;

	@Value("${captcha.slide.slider.size:5}")
	private String  captchaSlideSliderSize;

	@Value("${captcha.slide.background.width:280}")
	private String  captchaSlideBackgroundWidth;

	@Value("${captcha.slide.background.height:100}")
	private String captchaSlideBackgroundHeight;

	@Value("${captcha.slide.slider.left:20}")
	private String captchaSlideSliderLeft;

	@Value("${captcha.slide.slider.right:250}")
	private String captchaSlideSliderRight;

	@Value("${captcha.slide.slider.top:5}")
	private String captchaSlideSliderTop;

	@Value("${captcha.slide.slider.bottom:95}")
	private String captchaSlideSliderBottom;

	@Value("${captcha.slide.offset:5}")
	private String captchaSlideOffset;

	@Value("${captcha.slide.slider.side:40}")
	private String captchaSlideSliderSide;

	@Value("${captcha.slide.slider.radius:5}")
	private String captchaSlideSliderRadius;

	@Value("${captcha.slide.slider.overlap:2}")
	private String captchaSlideSliderOverlap;

	@Value("${captcha.slide.filter.radius:20}")
	private String captchaSlideFilterRadius;

	@Value("${captcha.slide.slider.alpha:0.9}")
	private String captchaSlideSliderAlpha;

	@Value("${captcha.slide.timeout:120}")
	private String captchaSlideTimeout;

	@Value("${captcha.slide.border.width:3}")
	private String captchaSlideBorderWidth;

	@Value("${captcha.slide.border.alpha:0.9}")
	private String captchaSlideBorderAlpha;

	/**
	 * 获取验证码类型
	 *
	 * @return 验证码类型
	 */
	public String getCaptchaType() {
		return captchaType;
	}

	/**
	 * 获取滑动验证码原始图片的路径
	 *
	 * @return 滑动验证码原始图片的路径
	 */
	public String getSlideImagePath() {
		return captchaSlideImagePath;
	}

	/**
	 * 获取滑块验证码背景图片个数
	 */
	public Integer getSlideImageSize() {
		String sliderSideConfig = captchaSlideSliderSize;
		if (StringUtils.isEmpty(sliderSideConfig)) {
			return null;
		}
		return Integer.valueOf(sliderSideConfig);
	}

	/**
	 * 获取滑动验证码背景图片的宽度
	 *
	 * @return 滑动验证码背景图片的宽度
	 */
	public Integer getSlideBackgroundWidth() {
		String backgroundWidthConfig = captchaSlideBackgroundWidth;
		if (StringUtils.isEmpty(backgroundWidthConfig)) {
			return null;
		}

		return Integer.valueOf(backgroundWidthConfig);
	}

	/**
	 * 获取滑动验证码背景图片的高度
	 *
	 * @return 滑动验证码背景图片的高度
	 */
	public Integer getSlideBackgroundHeight() {
		String backgroundHeightConfig = captchaSlideBackgroundHeight;
		if (StringUtils.isEmpty(backgroundHeightConfig)) {
			return null;
		}

		return Integer.valueOf(backgroundHeightConfig);
	}

	/**
	 * 获取滑动验证码，滑块左侧边界值，此参数可避免生成的滑块的位置太靠近图片的左边
	 */
	public Integer getSlideSliderLeft() {
		String sliderLeftConfig = captchaSlideSliderLeft;
		if (StringUtils.isEmpty(sliderLeftConfig)) {
			return null;
		}

		return Integer.valueOf(sliderLeftConfig);
	}

	/**
	 * 获取滑动验证码，滑块右侧边界值，此参数可避免生成的滑块太靠近图片的右边
	 */
	public Integer getSlideSliderRight() {
		String sliderRightConfig = captchaSlideSliderRight;
		if (StringUtils.isEmpty(sliderRightConfig)) {
			return null;
		}

		return Integer.valueOf(sliderRightConfig);
	}

	/**
	 * 获取滑动验证码，滑块顶部边界值，避免生成的滑块太靠近图片的顶部
	 */
	public Integer getSlideSliderTop() {
		String sliderTopConfig = captchaSlideSliderTop;
		if (StringUtils.isEmpty(sliderTopConfig)) {
			return null;
		}

		return Integer.valueOf(sliderTopConfig);
	}

	/**
	 * 获取滑动验证码，滑块底部边界值，此参数可避免生成的滑块太靠近图片的底部
	 */
	public Integer getSlideSliderBottom() {
		String sliderBottomConfig = captchaSlideSliderBottom;
		if (StringUtils.isEmpty(sliderBottomConfig)) {
			return null;
		}

		return Integer.valueOf(sliderBottomConfig);
	}

	/**
	 * 获取滑动验证码，验证验证位置时，坐标偏差
	 */
	public Integer getSlideOffset() {
		String offsetConfig = captchaSlideOffset;
		if (StringUtils.isEmpty(offsetConfig)) {
			return null;
		}

		return Integer.valueOf(offsetConfig);
	}

	/**
	 * 获取滑块验证码，滑块上方块边长
	 */
	public Integer getSlideSliderSide() {
		String sliderSideConfig = captchaSlideSliderSide;
		if (StringUtils.isEmpty(sliderSideConfig)) {
			return null;
		}

		return Integer.valueOf(sliderSideConfig);
	}

	/**
	 * 获取滑动验证码，滑块上小圆半径
	 */
	public Integer getSlideSliderRadius() {
		String sliderRadiusConfig = captchaSlideSliderRadius;
		if (StringUtils.isEmpty(sliderRadiusConfig)) {
			return null;
		}

		return Integer.valueOf(sliderRadiusConfig);
	}

	/**
	 * 获取滑动验证码，滑块上方块和小圆重叠部分像素值
	 */
	public Integer getSlideSliderOverlap() {
		String sliderOverlapConfig = captchaSlideSliderOverlap;
		if (StringUtils.isEmpty(sliderOverlapConfig)) {
			return null;
		}

		return Integer.valueOf(sliderOverlapConfig);
	}

	/**
	 * 获取滑动验证码，高斯模糊半径
	 *
	 * @return 滑动的alpha通道值（对比度）
	 */
	public Integer getSlideFilterRadius() {
		String filterRadius = captchaSlideFilterRadius;
		if (StringUtils.isEmpty(filterRadius)) {
			return null;
		}

		return Integer.valueOf(filterRadius);
	}

	/**
	 * 获取滑块验证码，背景图片上滑块水印的对比度
	 *
	 * @return 滑块水印对比度
	 */
	public Float getSlideSliderAlpha() {
		String sliderAlpha = captchaSlideSliderAlpha;
		if (StringUtils.isEmpty(sliderAlpha)) {
			return null;
		}

		return Float.parseFloat(sliderAlpha);
	}

	/**
	 * 获取滑动验证码过期时间，单位秒
	 */
	public Long getSlideTimeout() {
		String slideTimeout = captchaSlideTimeout;
		if (StringUtils.isEmpty(slideTimeout)) {
			return null;
		}

		return Long.valueOf(slideTimeout);
	}

	/**
	 * 获取滑块边框的宽度
	 *
	 * @return 滑块边框宽度
	 */
	public Integer getSlideBorderWidth() {
		String borderWidth = captchaSlideBorderWidth;
		if (StringUtils.isEmpty(borderWidth)) {
			return null;
		}

		return Integer.parseInt(borderWidth);
	}

	/**
	 * 获取滑块边框对比度
	 *
	 * @return 滑块边框对比度
	 */
	public Float getSlideBorderAlpha() {
		String borderAlpha = captchaSlideBorderAlpha;
		if (StringUtils.isEmpty(borderAlpha)) {
			return null;
		}

		return Float.parseFloat(borderAlpha);
	}
}
