package com.sinosoft.fragins.framework;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class PasswordPropEncryptorMain {

	public static void main(String[] args) {
		System.out.println(enc("sinosoft202003", "fragins"));
	}

	/**
	 * 进行加密
	 * 
	 * @param text     要加密的串
	 * @param password 加密用的密码
	 * @return 加密后的串
	 */
	public static String enc(String text, String password) {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(password);
		config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName(null);
		config.setProviderClassName(null);
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);
		return encryptor.encrypt(text);
	}

}
