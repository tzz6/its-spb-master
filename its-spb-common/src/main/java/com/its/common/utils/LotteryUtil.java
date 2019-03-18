package com.its.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 根据设定的概率随机抽取数据算法
 * 
 * @author tzz
 * @date 2019/02/22
 * @Introduce: Write describe here
 */
public class LotteryUtil {

	/** 初始化 */
	public List<RewardEntry> initRewards() {
		List<RewardEntry> rewardEntries = new ArrayList<RewardEntry>();
		rewardEntries.add(new RewardEntry("A", 5));
		rewardEntries.add(new RewardEntry("B", 25));
		rewardEntries.add(new RewardEntry("C", 5));
		rewardEntries.add(new RewardEntry("D", 35));
		rewardEntries.add(new RewardEntry("E", 5));
		rewardEntries.add(new RewardEntry("F", 25));
		return rewardEntries;
	}

	/** 获取编码数组 */
	public List<RewardEntry> getKeys(int lotteryNum) {
		List<RewardEntry> list = new ArrayList<RewardEntry>();
		for (int i = 0; i < lotteryNum; i++) {
			list.add(getKey());
		}
		return list;
	}

	/** 获取编码 */
	public RewardEntry getKey() {
		// 1.生成随机数列表
		List<RewardEntry> randomList = initRewards();
		Collections.shuffle(randomList);
		// 2.根据随机列表得到的概率区段
		List<Integer> percentSteps = getPercentSteps(randomList);
		// 3.随机抽取编号
		// 3.1 概率区段的最大值
		int maxPercentStep = percentSteps.get(percentSteps.size() - 1);
		// 3.2在概率区段范围内生成一个随机数
		int randomNum = new Random().nextInt(maxPercentStep);
		// 3.3获取对应的下标，得到抽中的编号
		int keyIndex = 0;
		for (int i = 0; i < percentSteps.size(); i++) {
			if (percentSteps.get(i) > randomNum) {
				keyIndex = i;
				break;
			}
		}
		return randomList.get(keyIndex);
	}

	/** 根据随机数列表生成概率区段如：[35, 60, 65, 70, 75, 100] */
	public List<Integer> getPercentSteps(List<RewardEntry> rewardEntryList) {
		List<Integer> percentSteps = new ArrayList<Integer>();
		int percent = 0;
		for (RewardEntry re : rewardEntryList) {
			percent += re.getPercent();
			percentSteps.add(percent);
		}
		return percentSteps;
	}
}
