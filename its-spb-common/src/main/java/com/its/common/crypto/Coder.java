package com.its.common.crypto;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Coder {

	public static String encryptBASE64(byte[] key) {  
		return (new BASE64Encoder()).encodeBuffer(key);  
	}
	
	public static byte[] decryptBASE64(String key) throws IOException {  
		return new BASE64Decoder().decodeBuffer(key);
	}
}
