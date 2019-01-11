package com.its.common.crypto.certificate;

import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;

import sun.security.x509.*;

public class SignCert {

	private String mKeystore = "d:/tzz-sf.keystore"; // 密锁库路径
	private char[] mKeystorePass = "123456789".toCharArray();// 密锁库密码
	private char[] mSignPrivateKeyPass = "123456789".toCharArray();// 取得签发者私锁所需的密码
	private String mSignCertAlias = "www.tzz-sf.com";// 签发者别名
	private String mSignedCert = "d:/tzz-sf.cer"; // 被签证书
	private String mNewCert = "d:/sign-tzz-sf.cer"; // 签发后的新证书全名
	private int mValidityDay = 1; // 签发后的新证书有效期（天）

	private PrivateKey mSignPrivateKey = null;// 签发者的私锁
	private X509CertInfo mSignCertInfo = null;// 签发证书信息
	private X509CertInfo mSignedCertInfo = null;// 被签证书信息

	/** 证书签名*/
	public void sign() {
		try {
			getSignCertInfo(); // 获取签名证书信息
			signCertificate(); // 用签名证书信息签发待签名证书
			createNewCertificate(); // 创建并保存签名后的新证书
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**取得签名证书信息*/
	private void getSignCertInfo() throws Exception {
		FileInputStream vFin = null;
		KeyStore vKeyStore = null;
		java.security.cert.Certificate vCert = null;
		X509CertImpl vCertImpl = null;
		byte[] vCertData = null;

		// 获取签名证书密锁库
		vFin = new FileInputStream(mKeystore);
		vKeyStore = KeyStore.getInstance("JKS");
		vKeyStore.load(vFin, mKeystorePass);
		// 获取签名证书
		vCert = vKeyStore.getCertificate(mSignCertAlias);
		vCertData = vCert.getEncoded();
		vCertImpl = new X509CertImpl(vCertData);
		// 获取签名证书信息
		mSignCertInfo = (X509CertInfo) vCertImpl.get(X509CertImpl.NAME + "." + X509CertImpl.INFO);
		mSignPrivateKey = (PrivateKey) vKeyStore.getKey(mSignCertAlias, mSignPrivateKeyPass);
		vFin.close();
	}

	/**取得待签证书信息，并签名待签证书*/
	private void signCertificate() throws Exception {
		FileInputStream vFin = null;
		java.security.cert.Certificate vCert = null;
		CertificateFactory vCertFactory = null;
		byte[] vCertData = null;
		X509CertImpl vCertImpl = null;

		// 获取待签名证书
		vFin = new FileInputStream(mSignedCert);
		vCertFactory = CertificateFactory.getInstance("X.509");
		vCert = vCertFactory.generateCertificate(vFin);
		vFin.close();
		vCertData = vCert.getEncoded();
		// 设置签名证书信息：有效日期、序列号、签名者、数字签名算发
		vCertImpl = new X509CertImpl(vCertData);
		mSignedCertInfo = (X509CertInfo) vCertImpl.get(X509CertImpl.NAME + "." + X509CertImpl.INFO);
		mSignedCertInfo.set(X509CertInfo.VALIDITY, getCertValidity());
		mSignedCertInfo.set(X509CertInfo.SERIAL_NUMBER, getCertSerualNumber());
		mSignedCertInfo.set(X509CertInfo.ISSUER + "." + CertificateIssuerName.DN_NAME,
				mSignCertInfo.get(X509CertInfo.SUBJECT + "." + CertificateIssuerName.DN_NAME));
		mSignedCertInfo.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, getAlgorithm());

	}

	/**待签签证书被签名后，保存新证书*/
	private void createNewCertificate() throws Exception {
		FileOutputStream vOut = null;
		X509CertImpl vCertImpl = null;
		// 用新证书信息封成为新X.509证书
		vCertImpl = new X509CertImpl(mSignedCertInfo);
		// 生成新正书验证码
		vCertImpl.sign(mSignPrivateKey, "MD5WithRSA");
		vOut = new FileOutputStream(mNewCert);
		// 保存为der编码二进制X.509格式证书
		vCertImpl.derEncode(vOut);
		vOut.close();
	}

	/** 得到新证书有效日期*/
	private CertificateValidity getCertValidity() throws Exception {
		long vValidity = (60 * 60 * 24 * 1000L) * mValidityDay;
		Calendar vCal = null;
		Date vBeginDate = null, vEndDate = null;
		vCal = Calendar.getInstance();
		vBeginDate = vCal.getTime();
		vEndDate = vCal.getTime();
		vEndDate.setTime(vBeginDate.getTime() + vValidity);
		return new CertificateValidity(vBeginDate, vEndDate);
	}

	/**得到新证书的序列号*/
	private CertificateSerialNumber getCertSerualNumber() {
		Calendar vCal = null;
		vCal = Calendar.getInstance();
		int vSerialNum = 0;
		vSerialNum = (int) (vCal.getTimeInMillis() / 1000);
		return new CertificateSerialNumber(vSerialNum);
	}

	/**得到新证书的签名算法*/
	private AlgorithmId getAlgorithm() {
		AlgorithmId vAlgorithm = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
		return vAlgorithm;
	}

}
