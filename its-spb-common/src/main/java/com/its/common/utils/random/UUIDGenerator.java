package com.its.common.utils.random;

import java.net.InetAddress;

/**
 * 生成类似hibernate中uuid 32位主键序列
 * 
 * @author tzz
 */
public class UUIDGenerator {

    private static final int IP;

    public static int ipToInt(byte[] bytes) {
        int result = 0;
        int index = 4;
        for (int i = 0; i < index; i++) {
            result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }

    static {
        int ipadd;
        try {
            ipadd = ipToInt(InetAddress.getLocalHost().getAddress());
        } catch (Exception e) {
            ipadd = 0;
        }
        IP = ipadd;
    }
    private static short counter = (short) 0;
    private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

    public UUIDGenerator() {
    }
    public static int getJVM() {
        return JVM;
    }
    public static short getCount() {
        synchronized (UUIDGenerator.class) {
            if (counter < 0){
                counter = 0;
            }
            return counter++;
        }
    }
    public static int getIP() {
        return IP;
    }
    public static short getHiTime() {
        return (short) (System.currentTimeMillis() >>> 32);
    }

    public static int getLoTime() {
        return (int) System.currentTimeMillis();
    }

    private final static String SEP = "";

    public static String format(int intval) {
        String formatted = Integer.toHexString(intval);
        StringBuffer buf = new StringBuffer("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    public  static String format(short shortval) {
        String formatted = Integer.toHexString(shortval);
        StringBuffer buf = new StringBuffer("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    public static String generate() {
        return String.valueOf(new StringBuffer(36).append(format(getIP())).append(SEP)
                .append(format(getJVM())).append(SEP)
                .append(format(getHiTime())).append(SEP)
                .append(format(getLoTime())).append(SEP)
                .append(format(getCount())).toString());
    }

}
