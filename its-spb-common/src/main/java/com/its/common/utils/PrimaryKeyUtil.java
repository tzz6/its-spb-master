package com.its.common.utils;

import java.util.UUID;

/**
 * 主键生成类
 * 
 * @author tzz
 *
 */
public final class PrimaryKeyUtil {
	private static final String PK_ERR_ID_PERFIX = "er";
	private static int PK_LENGTH = 32;

	/**
	 * 生成PK
	 * 
	 * @return
	 */
	public static String genPrimaryKey() {
		try {
			return getUuId();
		} catch (Exception e) {
			return genPkWhenPostErr();
		}
	}

	private static String getUuId() {
		UUID uuid = UUID.randomUUID();
		String uuidStr = uuid.toString().replaceAll("-", "").toUpperCase();
		return uuidStr;
	}

	private static String genPkWhenPostErr() {
		StringBuffer bf = new StringBuffer(PK_ERR_ID_PERFIX);
		String timeLongVar = String.valueOf(System.currentTimeMillis());
		bf.append(timeLongVar);
		java.util.Random rdm = new java.util.Random();
		String rdmVar = String.valueOf(rdm.nextLong());
		bf.append(rdmVar);
		String ret = (bf != null && bf.length() > PK_LENGTH) ? bf.toString().substring(0, PK_LENGTH) : bf.toString();
		return ret.toUpperCase();
	}
}