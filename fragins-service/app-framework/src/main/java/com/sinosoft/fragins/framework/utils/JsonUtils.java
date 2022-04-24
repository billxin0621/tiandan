package com.sinosoft.fragins.framework.utils;

import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.setDateFormat(new SmartDateFormat(false));
		objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static String stringify(Object obj) {
		try {
			String json = objectMapper.writeValueAsString(obj);
			return json;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T parse(String json, Class<T> cls) {
		try {
			T obj = objectMapper.readValue(json, cls);
			return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
