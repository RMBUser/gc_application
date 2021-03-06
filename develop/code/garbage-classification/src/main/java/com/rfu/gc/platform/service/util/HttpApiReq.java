package com.rfu.gc.platform.service.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rfu.gc.platform.pub.util.ObjNullUtil;

@Service
@PropertySource("classpath:apis.properties")
public class HttpApiReq implements ApiReq {
	
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
		String requestBody = gson.toJson(entity, entity.getClass());
		return sendReq(urlStr, requestBody, null);
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
	public String sendReq(String urlStr, String requestBody) throws IOException {
		return sendReq(urlStr, requestBody, null);
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
	public String sendReq(String urlStr, Map<String, ?> args, String method) throws IOException {
		return sendReq(urlStr, args, method, null);
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
		charset = ObjNullUtil.emptyOrNull(charset) ? UTF8 : charset;
		String result = null;
		boolean reqSuccess = false;
		int reqTimes = 1;
		while (!reqSuccess) {
			try {
				result = callhttp(urlStr, requestBody, POST, charset);
				reqSuccess = true;
			} catch (IOException e) {
				if((++reqTimes)>this.reRequestTimes) throw e;
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
	 * @param charset:   charset.If this parameter is null or empty,utf-8 will be
	 *                   used as default
	 * @return response body as a string
	 * @throws IOException 
	 */
	@Override
	public String sendReq(String urlStr, Map<String, ?> argsMap, String method, String charset) throws IOException {
		if (ObjNullUtil.emptyOrNull(urlStr))
			throw new IllegalArgumentException("url string can not be empty");
		method = ObjNullUtil.emptyOrNull(method) ? GET : method;
		charset = ObjNullUtil.emptyOrNull(charset) ? UTF8 : charset;
		String args = null;
		if (ObjNullUtil.noEmptyOrNull(argsMap)) {
			if (GET.equals(method))
				urlStr = ApiReq.concatGetUrl(urlStr, argsMap, charset);
			else if (POST.equals(method))
				args = map2Json(argsMap);
		}
		String result = null;
		boolean reqSuccess = false;
		int reqTimes = 1;
		while (!reqSuccess) {
			try {
				result = callhttp(urlStr, args, method, charset);
				reqSuccess = true;
			} catch (IOException e) {
				if((++reqTimes)>this.reRequestTimes) throw e;
			}
		}
		return result;
	}

	private String map2Json(Map<String, ?> argsMap) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		return gson.toJson(argsMap, Map.class);
	}

	private String callhttp(String urlStr, String args, String method, String charset) throws IOException {
		URL url = null;
		HttpURLConnection conn = null;
		StringBuilder sb = new StringBuilder();
		BufferedWriter bw = null;
		BufferedReader br = null;
		try {
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);
			conn.setDoInput(true);
			if (POST.equals(method)) {
				conn.addRequestProperty("Content-Type", "application/json");
				conn.setDoOutput(true);
			}
			conn.setConnectTimeout(5000);
			conn.setUseCaches(false);
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			conn.connect();
			if (POST.equals(method) && ObjNullUtil.noEmptyOrNull(args)) {
				bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), charset));
				bw.write(args);
				bw.flush();
			}
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String temp;
			while ((temp = br.readLine()) != null)
				sb.append(temp);
		} catch (IOException e) {
			throw e;
		} finally {
			if (conn != null)
				conn.disconnect();
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					throw new RuntimeException();
				} finally {
					bw = null;
				}
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					throw new RuntimeException();
				} finally {
					br = null;
				}
		}
		return sb.toString();
	}
}
