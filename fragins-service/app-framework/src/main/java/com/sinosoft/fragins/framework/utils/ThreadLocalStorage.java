package com.sinosoft.fragins.framework.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 提供线程独立存储的静态方法，方便调用。 为防止内存泄漏，key不要使用和业务号之类相关的每次会不同的东西。
 */
public class ThreadLocalStorage {

	private static ThreadLocal<Map<String, Object>> storage = new ThreadLocal<>();

	public static Object get(String key) {
		return storage.get() == null ? null : storage.get().get(key);
	}

	public static void set(String key, Object value) {
		Map<String, Object> map = storage.get();
		if (map == null) {
			map = new HashMap<>();
			storage.set(map);
		}
		map.put(key, value);
	}

	public static void remove(String key) {
		Map<String, Object> map = storage.get();
		if (map != null) {
			map.remove(key);
		}
	}

	private ThreadLocalStorage() {
	}

}
