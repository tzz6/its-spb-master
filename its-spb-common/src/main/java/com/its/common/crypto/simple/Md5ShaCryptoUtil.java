package com.its.common.crypto.simple;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author tzz
 */
public class Md5ShaCryptoUtil {

    /** md5加密 */
    public static String md5Encrypt(String str) {
        String md5String = null;
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            md.update(str.getBytes());
            // 获得密文，把密文转换成十六进制的字符串形式
            // 方式一
            md5String = byte2Hex(md.digest());
            // 方式二
            // md5String = byteToHex(md.digest());
            // 方式三
            // md5String = byteToString(md.digest());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return md5String;
    }

    /** SHA1加密 */
    public static String shaEncrypt(String date) {
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
            rs = byte2Hex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /** SHA512加密 */
    public static String sha512Encrypt(String date) {
        byte[] digest = null;
        String rs = null;
        try {
            // 得到一个SHA-1的消息摘要
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            // 添加要进行计算摘要的信息
            messageDigest.update(date.getBytes());
            // 得到该摘要
            digest = messageDigest.digest();
            // 将摘要转为字符串
            rs = byte2Hex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /** 1.把密文转换成十六进制的字符串形式（Integer.toHexString函数） */
    public static String byte2Hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        String tmp = "";
        for (int i = 0; i < b.length; i++) {
            tmp = Integer.toHexString(b[i] & 0XFF);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString();
    }

    /** 2.把密文转换成十六进制的字符串形式（自定义） */
    public static String byteToHex(byte[] b) {
        // 全局数组
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        // 把密文转换成十六进制的字符串形式
        int j = b.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = b[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    /** 3.转换字节数组为16进制字串 */
    @SuppressWarnings("unused")
    private static String byteToString(byte[] b) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sBuffer.append(byteToArrayString(b[i]));
        }
        return sBuffer.toString();
    }

    /** 返回形式为数字跟字符串 */
    private static String byteToArrayString(byte b) {
        String[] strDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        int iRet = b;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

}
