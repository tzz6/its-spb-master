package com.its.common.crypto.des;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DES对称加密
 * 
 * @author tzz
 */
public class DesUtil {

	public static String ALGORITHM = "DES";
	public static String KEY = "QWE!@#123qwe123@";

	public static String encryptBASE64(byte[] key) {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	public static byte[] decryptBASE64(String key) {
		try {
			return new BASE64Decoder().decodeBuffer(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 生成密钥串 */
	public static String initKey(String seed) {
		String key = null;
		try {
			SecureRandom secureRandom = null;
			secureRandom = new SecureRandom(decryptBASE64(seed));
			KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
			kg.init(secureRandom);
			SecretKey secretKey = kg.generateKey();
			key = encryptBASE64(secretKey.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return key;
	}

	/** 创建密钥对象 */
	public static Key createKey(byte[] key) {
		SecretKey secretKey = null;
		try {
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			secretKey = keyFactory.generateSecret(dks);
			// 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
			// secretKey = new SecretKeySpec(key, ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return secretKey;
	}

	/** 加密 */
	public static byte[] encrypt(byte[] data, Key key) {
	    // 定义要生成的密文
		byte[] cipherByte = null;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			// 加密随机数生成器 (可以不写)
			SecureRandom sr = new SecureRandom();
			cipher.init(Cipher.ENCRYPT_MODE, key, sr);
			cipherByte = cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherByte;
	}

	/** 解密 */
	public static byte[] decrypt(byte[] data, Key key) {
	    // 定义要生成的密文
		byte[] cipherByte = null;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			// 加密随机数生成器 (可以不写)
			SecureRandom sr = new SecureRandom();
			cipher.init(Cipher.DECRYPT_MODE, key, sr);
			cipherByte = cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherByte;
	}

	public static String encrypt(String content, String keyStr) {
		System.out.println("加密前:" + content + "密钥:" + keyStr);
		keyStr = initKey(keyStr);
		Key key = createKey(decryptBASE64(keyStr));
		byte[] encryptByte = encrypt(content.getBytes(), key);
		String encryptStr = encryptBASE64(encryptByte);
		return encryptStr;
	}

	public static String decrypt(String content, String keyStr) {
		System.out.println("加密后:" + content + "密钥:" + keyStr);
		keyStr = initKey(keyStr);
		Key key = createKey(decryptBASE64(keyStr));
		byte[] decryptByte = decrypt(decryptBASE64(content), key);
		String decryptStr = new String(decryptByte);
		return decryptStr;
	}
}
