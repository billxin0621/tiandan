package com.sinosoft.fragins.framework.utils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * HTTP返回对象
 */
@SuppressWarnings("serial")
public class HttpResponse implements Serializable {

	private int statusCode;

	private String statusMessage;

	private Map<String, String> headers = new LinkedHashMap<>();

	private String encoding = "UTF-8";

	private byte[] body;

	public HttpResponse(int statusCode, String statusMessage, Map<String, String> headers, byte[] body) {
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.headers.putAll(headers);
		this.body = body;
		String contentType = this.headers.get("content-type");
		if (contentType != null) {
			int charsetIndex = contentType.toLowerCase().indexOf("charset=");
			if (charsetIndex > 0) {
				encoding = contentType.substring(charsetIndex + 8).trim();
			}
		}
	}

	/** 获取Http返回状态码 */
	public int getStatusCode() {
		return statusCode;
	}

	/** 获取Http返回状态信息 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/** 获取Http响应头信息 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/** 获取Http响应头信息 */
	public String getHeader(String name) {
		return headers.get(name);
	}

	/** 获取Http响应体 */
	public byte[] getBody() {
		return body;
	}

	/** 字符串方式获取Http响应体 */
	public String getBodyAsString() {
		return getBodyAsString(encoding);
	}

	/** 字符串方式获取Http响应体 */
	public String getBodyAsString(String encoding) {
		if (StringUtils.isBlank(encoding)) {
			encoding = this.encoding;
		}
		try {
			return new String(body, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
