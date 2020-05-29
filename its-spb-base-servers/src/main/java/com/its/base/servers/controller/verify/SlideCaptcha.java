package com.its.base.servers.controller.verify;

import java.io.Serializable;

/**
 * 滑动验证码
 *
 * @author 01389232
 *
 */
public class SlideCaptcha implements Serializable {
	private static final long serialVersionUID = 7254543959813447083L;

	/**
	 * 滑块图片数据，base64格式文本
	 */
	private String sliderImage;

	/**
	 * 背景图片数据，base64格式文本
	 */
	private String backgroundImage;

	/**
	 * 背景图片宽度
	 */
	private int backgroundWidth;

	public String getSliderImage() {
		return sliderImage;
	}

	public void setSliderImage(String sliderImage) {
		this.sliderImage = sliderImage;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public int getBackgroundWidth() {
		return backgroundWidth;
	}

	public void setBackgroundWidth(int backgroundWidth) {
		this.backgroundWidth = backgroundWidth;
	}
}
