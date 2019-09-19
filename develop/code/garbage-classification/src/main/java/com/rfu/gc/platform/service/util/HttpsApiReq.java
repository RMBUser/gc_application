package com.rfu.gc.platform.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rfu.gc.platform.pub.util.ObjNullUtil;

/**
 * Establish connection with https protocol
 * 
 * @author Cris
 * 
 */
@Service
@PropertySource("classpath:apis.properties")
public class HttpsApiReq implements ApiReq {
	
	@Value("${apis.re-request.times}")
	private int reRequestTimes;

	/**
	 * Entity is used to send a <strong>POST</strong> request as requestBody json
	 * and using utf-8 by default
	 * 
	 * @param urlStr:url   string
	 * @param entity:which will be casted to a json string and used as request body
	 * @return response body as a string
	 * @throws IOException
	 */
	@Override
	public <E> String sendReq(String urlStr, E entity) throws IOException {
		return sendReq(urlStr, entity, null);
	}

	/**
	 * Entity is used to send a <strong>POST</strong> request as requestBody json
	 * 
	 * @param urlStr:url   string
	 * @param entity:which will be casted to a json string and used as request body
	 * @param charset:     charset.If this parameter is null or empty,utf-8 will be
	 *                     used as default
	 * @return response body as a string
	 * @throws IOException
	 */
	@Override
	public <E> String sendReq(String urlStr, E entity, String charset) throws IOException {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String jsonBody = gson.toJson(entity, entity.getClass());
		return sendReq(urlStr, jsonBody, null);
	}

	/**
	 * Send a <strong>POST</strong> request and default utf-8
	 * 
	 * @param urlStr:url   string
	 * @param requestBody: will be casted to a json string and used as request body
	 * @return response body as a string
	 * @throws IOException
	 */
	@Override
	public String sendReq(String urlStr, String requstBody) throws IOException {
		return sendReq(urlStr, requstBody, null);
	}

	/**
	 * Send a <strong>POST</strong> request and default utf-8
	 * 
	 * @param urlStr:url   string
	 * @param requestBody: will be casted to a json string and used as request body
	 * @param charset:     charset.If this parameter is null or empty,utf-8 will be
	 *                     used as default
	 * @return response body as a string
	 * @throws IOException
	 */
	@Override
	public String sendReq(String urlStr, String requestBody, String charset) throws IOException {
		if (ObjNullUtil.emptyOrNull(urlStr))
			throw new IllegalArgumentException("url string can not be empty");
		charset = charset == null ? UTF8 : charset;
		String result = null;
		boolean reqSuccess = false;
		int reqTimes = 1;
		while (!reqSuccess) {
			try {
				result = callhttps(urlStr, requestBody, POST, charset);
				reqSuccess = true;
			} catch (IOException e) {
				if ((++reqTimes) > this.reRequestTimes)
					throw e;
			}
		}
		return result;
	}

	/**
	 * Use argsMap to send request.<br/>
	 * example:</br>
	 * Get:www.google.com/a/b?type=c&name=d <br/>
	 * Post:www.google.com/a/b [requestBody:{"type":"c","name":"d"}]
	 * 
	 * @param urlStr:url string
	 * @param argsMap:   if it is get request,url and elements in the map will be
	 *                   put together,or it will casted to a json string and used as
	 *                   request body
	 * @param method:    request method.If this parameter is null or
	 *                   empty,<Strong>Get</Strong> will be used as default
	 * @return response body as a string
	 * @throws IOException
	 */
	@Override
	public String sendReq(String urlStr, Map<String, ?> argsMap, String method) throws IOException {
		return sendReq(urlStr, argsMap, method, null);
	}

	/**
	 * Use argsMap to send request.<br/>
	 * example:</br>
	 * Get:www.google.com/a/b?type=c&name=d <br/>
	 * Post:www.google.com/a/b [requestBody:{"type":"c","name":"d"}]
	 * 
	 * @param urlStr:url string
	 * @param argsMap:   if it is get request,url and elements in the map will be
	 *                   put together,or it will casted to a json string and used as
	 *                   request body
	 * @param method:    request method.If this parameter is null or
	 *                   empty,<Strong>Get</Strong> will be used as default
	 * @param charset:   charset.If this parameter is null or empty,utf-8 will be
	 *                   used as default
	 * @return response body as a string
	 * @throws IOException
	 */
	@Override
	public String sendReq(String urlStr, Map<String, ?> argsMap, String method, String charset) throws IOException {
		if (ObjNullUtil.emptyOrNull(urlStr))
			throw new IllegalArgumentException("url string can not be empty");
		charset = ObjNullUtil.emptyOrNull(charset) ? UTF8 : charset;
		method = ObjNullUtil.emptyOrNull(method) ? GET : method;
		String requestBody = null;
		if (ObjNullUtil.noEmptyOrNull(argsMap)) {
			if (GET.equals(method))
				urlStr = ApiReq.concatGetUrl(urlStr, argsMap);
			else if (POST.equals(method))
				requestBody = map2Json(argsMap);
		}
		String result = null;
		boolean reqSuccess = false;
		int reqTimes = 1;
		while (!reqSuccess) {
			try {
				result = callhttps(urlStr, requestBody, method, charset);
				reqSuccess = true;
			} catch (IOException e) {
				if ((++reqTimes) > this.reRequestTimes)
					throw e;
			}
		}
		return result;
	}

	private String callhttps(String urlStr, String requestBody, String method, String charset) throws IOException {
		URL url = null;
		HttpsURLConnection conn = null;
		OutputStream os = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			url = new URL(urlStr);
			conn = (HttpsURLConnection) url.openConnection();
//			conn.setSSLSocketFactory(var1);
			conn.setConnectTimeout(5000);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(method);
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.19 (KHTML, like Gecko) Ubuntu/11.10 Chromium/18.0.1025.142 Chrome/18.0.1025.142 Safari/535.19");
			if (POST.equals(method)) {
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-type", "application/json");
			}
			conn.connect();
			if (POST.equals(method) && ObjNullUtil.noEmptyOrNull(requestBody)) {
				os = conn.getOutputStream();
				os.write(requestBody.getBytes(charset));
				os.flush();
			}
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String temp;
			if ((temp = br.readLine()) != null)
				sb.append(temp);
		} catch (IOException e) {
			throw e;
		} finally {
			if (conn != null)
				conn.disconnect();
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					os = null;
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					br = null;
				}
			}
		}
		return sb.toString();
	}

	private String map2Json(Map<String, ?> argsMap) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		return gson.toJson(argsMap, Map.class);
	}

}
