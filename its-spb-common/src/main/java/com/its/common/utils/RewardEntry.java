package com.its.common.utils;

/**
 * 模型
 * @author tzz
 * @date 2019/02/22
 * @Introduce: Write describe here
 */
public class RewardEntry {

    /** 名称 */
	private String name;
	/** 概率 */
	private int percent;

	public RewardEntry(String name, int percent) {
		super();
		this.name = name;
		this.percent = percent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPercent() {
		return percent;
	}
	public void setPercent(int percent) {
		this.percent = percent;
	}
}
