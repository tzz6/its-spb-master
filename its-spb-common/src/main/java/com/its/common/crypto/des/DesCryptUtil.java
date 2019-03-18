package com.its.common.crypto.des;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 对称加密(DES/3DES/AES) 推荐使用AES
 * 
 * @author tzz
 */
public class DesCryptUtil {

	/**
	 * 生成一个指定算法的密匙
	 * 
	 * @param algorithm
	 *            加密算法
	 * @return
	 */
	public static SecretKey createSecretKey(String algorithm) {
		// 声明KeyGenerator对象
		KeyGenerator keygen;
		// 声明 密钥对象
		SecretKey deskey = null;
		try {
			// 返回生成指定算法的秘密密钥的 KeyGenerator 对象
			keygen = KeyGenerator.getInstance(algorithm);
			// 生成一个密钥
			deskey = keygen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 返回密匙
		return deskey;
	}

	/**
	 * 生成一个指定算法的密匙
	 * 
	 * @param algorithm
	 *            加密算法
	 * @param key
	 * @return
	 */
	public static SecretKey createSecretKey(String algorithm, String key) {
		// 声明 密钥对象
		SecretKey secretKey = null;
		SecretKeyFactory secretKeyFactory = null;
		try {
			secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
			DESKeySpec keyspec = new DESKeySpec(key.getBytes());
			// 生成一个密钥
			secretKey = secretKeyFactory.generateSecret(keyspec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回密匙
		return secretKey;
	}

	/** 加密 */
	public static String encrypt(String algorithm, SecretKey key, String date) {
	    // 定义要生成的密文
		byte[] cipherByte = null;
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			// 加密随机数生成器 (可以不写)
			SecureRandom sr = new SecureRandom();
			// 用指定的密钥和模式初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, key, sr);
			// 对要加密的内容进行编码处理,
			cipherByte = cipher.doFinal(date.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回密文的十六进制形式
		return byte2hex(cipherByte);
	}
	
	/** 加密 */
	public static byte[] encryptByte(String algorithm, SecretKey key, String date) {
	    // 定义要生成的密文
		byte[] cipherByte = null;
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			// 加密随机数生成器 (可以不写)
			SecureRandom sr = new SecureRandom();
			// 用指定的密钥和模式初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, key, sr);
			// 对要加密的内容进行编码处理,
			cipherByte = cipher.doFinal(date.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回密文的十六进制形式
		return cipherByte;
	}

	/** 解密 */
	public static String decrypt(String algorithm, SecretKey key, String date) {
	    // 加密随机数生成器
		SecureRandom sr = new SecureRandom();
		byte[] cipherByte = null;
		try {
			// 得到加密/解密器
			Cipher cipher = Cipher.getInstance(algorithm);
			// 用指定的密钥和模式初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, key, sr);
			// 对要解密的内容进行编码处理
			cipherByte = cipher.doFinal(hex2byte(date));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(cipherByte);
	}
	
	/** 解密 */
	public static String decryptByte(String algorithm, SecretKey key, byte[] date) {
	    // 加密随机数生成器
		SecureRandom sr = new SecureRandom();
		byte[] cipherByte = null;
		try {
			// 得到加密/解密器
			Cipher cipher = Cipher.getInstance(algorithm);
			// 用指定的密钥和模式初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, key, sr);
			// 对要解密的内容进行编码处理
			cipherByte = cipher.doFinal(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(cipherByte);
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		StringBuilder builder = new StringBuilder();
		builder.append("");
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
			    builder.append("0").append(stmp);
			} else {
			    builder.append(stmp);
			}
		}
		hs = builder.toString();
		return hs.toUpperCase();
	}

    public static byte[] hex2byte(String hex) {
        int index = 8;
        byte[] ret = new byte[index];
        byte[] tmp = hex.getBytes();

        for (int i = 0; i < index; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

	public static byte uniteBytes(byte src0, byte src1) {
		byte b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
		b0 = (byte) (b0 << 4);
		byte b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (b0 ^ b1);
		return ret;
	}
}
