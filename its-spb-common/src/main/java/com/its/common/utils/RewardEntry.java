package com.its.common.utils;

/** 模型 */
public class RewardEntry {

	private String name;// 名称
	private int percent;// 概率

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
