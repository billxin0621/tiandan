package com.sinosoft.fragins.framework.utils;

import com.squareup.okhttp.*;
import com.squareup.okhttp.Request.Builder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Http客户端工具类，包装了一下OkHttpClient，提供调用外部Http接口的常用的GET/POST请求方式，方便代码中使用
 * <p>
 * 
 */
public class HttpClientUtils {

	/** 公共的OkHttpClient，需要的话可以直接使用 */
	public static OkHttpClient CLIENT;

	static {
		CLIENT = new OkHttpClient();
		CLIENT.setConnectTimeout(3000, TimeUnit.MILLISECONDS);
		CLIENT.setReadTimeout(300000, TimeUnit.MILLISECONDS);
	}

	/**
	 * 进行GET方式请求
	 * 
	 * @param url 地址，可以含请求参数部分
	 * @return Http响应信息
	 */
	public static HttpResponse get(String url) {
		return get(url, null);
	}

	/**
	 * 进行GET方式请求
	 * 
	 * @param url     地址，可以含请求参数部分
	 * @param headers 请求头
	 * @return Http响应信息
	 */
	public static HttpResponse get(String url, Map<String, String> headers) {
		try {
			Builder builder = new Request.Builder().get().url(url);
			addHeaders(builder, headers);
			Request request = builder.build();
			Response response = CLIENT.newCall(request).execute();
			return toHttpResponse(response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 进行GET方式请求，直接获取响应报文体。如果响应状态非200则抛出异常
	 * 
	 * @param url 地址，可以含请求参数部分
	 * @return 响应报文体
	 */
	public static String getBody(String url) {
		return getBody(url, null);
	}

	/**
	 * 进行GET方式请求，直接获取响应报文体。如果响应状态非200则抛出异常
	 * 
	 * @param url     地址，可以含请求参数部分
	 * @param headers 请求头
	 * @return 响应报文体
	 */
	public static String getBody(String url, Map<String, String> headers) {
		HttpResponse response = get(url, headers);
		if (response.getStatusCode() != 200) {
			throw new RuntimeException("Http Status " + response.getStatusCode());
		}
		return response.getBodyAsString();
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url       地址
	 * @param headers   请求头
	 * @param mediaType 请求体的格式，例如application/json或text/xml等，也可以包含charset=部分（<a href=
	 *                  "http://tools.ietf.org/html/rfc2045">RFC 2045</a>）
	 * @param body      请求体
	 * @return 响应
	 */
	public static HttpResponse post(String url, Map<String, String> headers, String mediaType, byte[] body) {
		try {
			Builder builder = new Request.Builder().post(RequestBody.create(MediaType.parse(mediaType), body)).url(url);
			addHeaders(builder, headers);
			Request request = builder.build();
			Response response = CLIENT.newCall(request).execute();
			return toHttpResponse(response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 发送JSON格式POST请求，content-type=application/json; charset=UTF-8
	 * 
	 * @param url     地址
	 * @param headers 请求头
	 * @param json    请求报文
	 * @return 响应
	 */
	public static HttpResponse postJson(String url, Map<String, String> headers, String json) {
		try {
			if (headers == null) {
				headers = new HashMap<String, String>();
			}
			addDefaultHeaders(headers);
			return post(url, headers, "application/json; charset=UTF-8", json.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 发送JSON格式POST请求，content-type=application/json; charset=UTF-8
	 * 
	 * @param url  地址
	 * @param json 请求报文
	 * @return 响应
	 */
	public static HttpResponse postJson(String url, String json) {
		return postJson(url, null, json);
	}

	/**
	 * 发送XML格式POST请求，content-type=text/xml; charset=UTF-8
	 * 
	 * @param url     地址
	 * @param headers 请求头
	 * @param json    请求报文
	 * @return 响应
	 */
	public static HttpResponse postXml(String url, Map<String, String> headers, String xml) {
		try {
			if (headers == null) {
				headers = new HashMap<String, String>();
			}
			addDefaultHeaders(headers);
			return post(url, headers, "text/xml; charset=UTF-8", xml.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 发送XML格式POST请求，content-type=text/xml; charset=UTF-8
	 * 
	 * @param url  地址
	 * @param json 请求报文
	 * @return 响应
	 */
	public static HttpResponse postXml(String url, String xml) {
		return postXml(url, null, xml);
	}

	/** postJson/postXml加一些默认头，模仿PostMan软件里面那些默认的头信息 */
	private static void addDefaultHeaders(Map<String, String> headers) {
		addDefaultHeader(headers, "Cache-Control", "no-cache");
		addDefaultHeader(headers, "Accept", "*/*");
		// addDefaultHeader(headers, "Accept-Encoding", "gzip, deflate, br");
	}

	private static void addDefaultHeader(Map<String, String> headers, String name, String value) {
		if (!headers.containsKey(name)) {
			headers.put(name, value);
		}
	}

	private static void addHeaders(Builder builder, Map<String, String> headers) {
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				builder.header(entry.getKey(), entry.getValue());
			}
		}
	}

	private static HttpResponse toHttpResponse(Response response) throws IOException {
		int statusCode = response.code();
		String statusMessage = response.message();
		Map<String, String> headers = new LinkedHashMap<>();
		Headers hs = response.headers();
		for (int i = 0; i < hs.size(); i++) {
			String name = hs.name(i);
			String value = hs.value(i);
			headers.put(name, value);
		}
		byte[] body = response.body().bytes();
		return new HttpResponse(statusCode, statusMessage, headers, body);
	}

}
