package com.its.common.crypto.simple;

import java.io.IOException;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * 
 * @author tzz
 */
public class Base64Util {
	private static final Logger logger = Logger.getLogger(Base64Util.class);

	/** 加密 */
	public static String encrypt(byte[] key) {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	/** 解密 */
	public static String decrypt(String key) {
		try {
			return new String((new BASE64Decoder()).decodeBuffer(key), "utf-8");
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		return null;
	}
}
