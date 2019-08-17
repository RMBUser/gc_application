package com.rfu.gc.platform.service.util;

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

	public <E extends Object> String sendReq(String urlStr, E entity) throws Exception;

	public <E extends Object> String sendReq(String urlStr, E entity, String charset) throws Exception;

	public String sendReq(String urlStr, String requstBody) throws Exception;

	public String sendReq(String urlStr, String requestBody, String charset) throws Exception;

	public String sendReq(String urlStr, Map<String, ?> argsMap, String method) throws Exception;

	public String sendReq(String urlStr, Map<String, ?> argsMap, String method, String charset) throws Exception;

	/**
	 * Splicing url and elements in map together
	 * 
	 * @param urlStr
	 * @param argsMap
	 * @return
	 * @throws Exception
	 */
	public static String concatGetUrl(String urlStr, Map<String, ?> argsMap) throws Exception {
		if (ObjNullUtil.emptyOrNull(urlStr) || ObjNullUtil.emptyOrNull(argsMap))
			throw new Exception("url or args can not be empty");
		StringBuilder sb = new StringBuilder(urlStr + "?");
		argsMap.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
		return sb.deleteCharAt(sb.length() - 1).toString();
	}
}
