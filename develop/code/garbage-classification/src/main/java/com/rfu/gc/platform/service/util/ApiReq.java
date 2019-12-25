package com.rfu.gc.platform.service.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.rfu.gc.platform.pub.util.ObjNullUtil;

/**
 * Send request api
 * 
 * @author Cris
 *
 */
public interface ApiReq {
	public static final String GET = "GET";

	public static final String POST = "POST";

	public static final String UTF8 = "utf-8";

	public static final String GBK = "gbk";

	/**
	 * Send request with entity.
	 * 
	 * @param urlStr
	 * @param entity
	 * @return response body as a string
	 * @throws IOException 
	 */
	public <E extends Object> String sendReq(String urlStr, E entity) throws IOException;

	/**
	 * Use specified character set to send request with entity .
	 * 
	 * @param urlStr
	 * @param entity
	 * @param charset
	 * @return response body as a string
	 * @throws IOException 
	 */
	public <E extends Object> String sendReq(String urlStr, E entity, String charset) throws IOException;

	/**
	 * Send request with a string request body
	 * 
	 * @param urlStr
	 * @param requestBody
	 * @return response body as a string
	 * @throws IOException 
	 */
	public String sendReq(String urlStr, String requstBody) throws IOException;

	/**
	 * Use specified character set to send request with a string request body
	 * 
	 * @param urlStr
	 * @param requestBody
	 * @param charset
	 * @return response body as a string
	 * @throws IOException 
	 */
	public String sendReq(String urlStr, String requestBody, String charset) throws IOException;

	/**
	 * Use specified request method to send request with a map argument
	 * 
	 * @param urlStr
	 * @param argsMap
	 * @param method
	 * @return response body as a string
	 * @throws IOException 
	 */
	public String sendReq(String urlStr, Map<String, ?> argsMap, String method) throws IOException;

	/**
	 * Use specified request method and character set to send request with a map
	 * argument
	 * 
	 * @param urlStr
	 * @param argsMap
	 * @param method
	 * @param charset
	 * @return response body as a string
	 * @throws IOException 
	 */
	public String sendReq(String urlStr, Map<String, ?> argsMap, String method, String charset) throws IOException;

	/**
	 * Splicing url and elements in map together
	 * 
	 * @param urlStr
	 * @param argsMap
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String concatGetUrl(String urlStr, Map<String, ?> argsMap, String charSet)
			throws UnsupportedEncodingException {
		if (ObjNullUtil.emptyOrNull(urlStr) || ObjNullUtil.emptyOrNull(argsMap))
			throw new IllegalArgumentException("url or args can not be empty");
		StringBuilder sb = new StringBuilder(urlStr + "?");
		for (String k : argsMap.keySet()) {
			if (argsMap.get(k) instanceof String) {
				sb.append(k).append("=").append(URLEncoder.encode(argsMap.get(k).toString(), charSet)).append("&");
			} else {
				sb.append(k).append("=").append(argsMap.get(k)).append("&");
			}
		}

		return sb.deleteCharAt(sb.length() - 1).toString();
	}
}
