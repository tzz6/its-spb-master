package com.its.common.crypto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 
 * @author tzz
 * @工号: 
 * @date 2019/02/22
 * @Introduce: Write describe here
 */
public class CryptUtil {

	/**MD5加密*/
	public static String encryptToMD5(String date) {
		byte[] digest = null;
		String rs = null;
		try {
			// 得到一个md5的消息摘要
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			// 添加要进行计算摘要的信息
			messageDigest.update(date.getBytes());
			// 得到该摘要
			digest = messageDigest.digest();
			// 将摘要转为字符串
			rs = byte2hex(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/**SHA1加密*/
	public static String encryptToSHA(String date) {
		byte[] digest = null;
		String rs = null;
		try {
			// 得到一个SHA-1的消息摘要
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			// 添加要进行计算摘要的信息
			messageDigest.update(date.getBytes());
			// 得到该摘要
			digest = messageDigest.digest();
			// 将摘要转为字符串
			rs = byte2hex(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/**生成一个DES算法的密匙*/
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
	 * 生成一个DES算法的密匙
	 * @param key指定密匙 
	 */
	public static SecretKey createSecretKey(String des, String key) {
		// 声明 密钥对象
		SecretKey secretKey = null;
		SecretKeyFactory secretKeyFactory = null;
		try {
			secretKeyFactory = SecretKeyFactory.getInstance(des);
			DESKeySpec keyspec = new DESKeySpec(key.getBytes());
			// 生成一个密钥
			secretKey = secretKeyFactory.generateSecret(keyspec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回密匙
		return secretKey;
	}
	
	public static String encryptToDES(SecretKey key, String date) {
	    //定义加密算法
		String algorithm = "DES";
		// 加密随机数生成器 (可以不写)
		SecureRandom sr = new SecureRandom();
		// 定义要生成的密文
		byte[] cipherByte = null;
		try {
		    //加密/解密器
			Cipher cipher = Cipher.getInstance(algorithm);
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
	
	public static String decryptByDES(SecretKey key, String date) {
	    //定义加密算法
		String algorithm = "DES";
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
//		 return byte2hex(cipherByte);
		return new String(cipherByte);
	}
	
	/**创建公匙和私匙*/
	public static void createPairKey() {
		try {
			// 根据特定的算法一个密钥对生成器
			KeyPairGenerator pairGenerator = KeyPairGenerator.getInstance("DSA");
			//加密随机数生成器
			SecureRandom sr = new SecureRandom();
			// 重新设置此随机对象的种子
			sr.setSeed(1000);
			// 使用给定的随机源（和默认的参数集合）初始化确定密钥大小的密钥对生成器
			// keygen.initialize(512);
			pairGenerator.initialize(512, sr);
			// 生成密钥组
			KeyPair keys = pairGenerator.generateKeyPair();
			// 得到公匙
			PublicKey pubkey = keys.getPublic();
			// 得到私匙
			PrivateKey prikey = keys.getPrivate();
			// 将公匙私匙写入到文件当中
			doObjToFile("mykeys.bat", new Object[] { prikey, pubkey });
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public static void signToInfo(String info, String signfile) {
		// 从文件当中读取私匙
		PrivateKey myprikey = (PrivateKey) getObjFromFile("mykeys.bat", 1);
		// 从文件中读取公匙
		PublicKey mypubkey = (PublicKey) getObjFromFile("mykeys.bat", 2);
		try {
			// Signature 对象可用来生成和验证数字签名
			Signature signet = Signature.getInstance("DSA");
			// 初始化签署签名的私钥
			signet.initSign(myprikey);
			// 更新要由字节签名或验证的数据
			signet.update(info.getBytes());
			// 签署或验证所有更新字节的签名，返回签名
			byte[] signed = signet.sign();
			
			// 将数字签名,公匙,信息放入文件中
			doObjToFile(signfile, new Object[] { signed, mypubkey, info });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean validateSign(String signfile) {
		// 读取公匙
		PublicKey mypubkey = (PublicKey) getObjFromFile(signfile, 2);
		// 读取签名
		byte[] signed = (byte[]) getObjFromFile(signfile, 1);
		// 读取信息
		String info = (String) getObjFromFile(signfile, 3);
		try {
			// 初始一个Signature对象,并用公钥和签名进行验证
			Signature signetcheck = Signature.getInstance("DSA");
			// 初始化验证签名的公钥
			signetcheck.initVerify(mypubkey);
			// 使用指定的 byte 数组更新要签名或验证的数据
			signetcheck.update(info.getBytes());
			// 验证传入的签名
			return signetcheck.verify(signed);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		StringBuilder stringBuilder = new StringBuilder();
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
			    stringBuilder.append("0").append(stmp);
			} else {
				stringBuilder.append(stmp);
			}
		}
		hs = stringBuilder.toString();
		return hs.toUpperCase();
	}
	
	public static byte[] hex2byte(String hex) {
		byte[] ret = new byte[8];
		byte[] tmp = hex.getBytes();
		int index = 8;
		for (int i = 0; i < index; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}
	
	public static byte uniteBytes(byte src0, byte src1) {
		byte b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		b0 = (byte) (b0 << 4);
		byte b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (b0 ^ b1);
		return ret;
	}
	
	public static void doObjToFile(String file, Object[] objs) {
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			for (int i = 0; i < objs.length; i++) {
				oos.writeObject(objs[i]);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				oos.flush();
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Object getObjFromFile(String file, int i) {
		ObjectInputStream ois = null;
		Object obj = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			for (int j = 0; j < i; j++) {
				obj = ois.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
}
