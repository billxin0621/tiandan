package com.sinosoft.fragins.framework.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 各类随机码生成工具
 */
public class RandomGenerator {

	private static final String NUMBER_CHARACTERS = "1234567890";
	private static final String CHARACTERS = "1234567890abcdefghijklmnopqrstuvwxyz";

	/**
	 * 产生只含数字的随机字符串
	 * 
	 * @param length 字符串长度
	 * @return 随机数字字符串
	 */
	public static String randomNumbers(int length) {
		return randomStringOfCharacters(NUMBER_CHARACTERS, length);
	}

	/**
	 * 产生含数字和小写字母的随机字符串
	 * 
	 * @param length 字符串长度
	 * @return 随机字符串
	 */
	public static String randomString(int length) {
		return randomStringOfCharacters(CHARACTERS, length);
	}

	/**
	 * 产生自定义随机字符串
	 * 
	 * @param characters 字符串中可包含的字符
	 * @param length     字符串长度
	 * @return 随机字符串
	 */
	public static String randomStringOfCharacters(String characters, int length) {
		StringBuilder sb = new StringBuilder();
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (int i = 0; i < length; i++) {
			char c = characters.charAt(random.nextInt(characters.length()));
			sb.append(c);
		}
		return sb.toString();
	}

	private RandomGenerator() {
	}

	/**
	 *
	 * @param bound
	 * @return bound以内的随机非负整数
	 */
	public static int randomNumOfBound(int bound) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		return random.nextInt(bound);
	}

}
