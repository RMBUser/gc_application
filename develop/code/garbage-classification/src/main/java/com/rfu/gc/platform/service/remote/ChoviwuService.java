package com.rfu.gc.platform.service.remote;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.rfu.gc.platform.entity.RemoteCallResultWrapper;
import com.rfu.gc.platform.entity.TypeOfGarbage;
import com.rfu.gc.platform.entity.choviwu.CwGarbageResponseBody;
import com.rfu.gc.platform.pub.util.ObjNullUtil;
import com.rfu.gc.platform.service.util.ApiReq;
import com.rfu.gc.platform.service.util.HttpsApiReq;

@Service
@PropertySource("apis.properties")
public class ChoviwuService {
	@Autowired
	private HttpsApiReq httpsApiReq;
	@Value("${api.gc.choviwu.apiAdress}")
	private String apiAdress;
	private static final Gson JSON_RESOLVER;

	static {
		JSON_RESOLVER = new Gson();
	}
	@Async("crisExecutor")
	public Future<RemoteCallResultWrapper<List<TypeOfGarbage>>> getTypeOfGarbage(String garbageName) {
		CwGarbageResponseBody cwGarbageResponseBody = callHttpsApi(garbageName);
		
		return new AsyncResult<RemoteCallResultWrapper<List<TypeOfGarbage>>>(null);
	}
	private CwGarbageResponseBody callHttpsApi(String garbageName) {
		Map<String, String> argsMap = new HashMap<>();
		argsMap.put("garbageName", garbageName);
		String returnJsonStr = null;
		try {
			returnJsonStr = httpsApiReq.sendReq(apiAdress, argsMap, ApiReq.GET);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CwGarbageResponseBody cwGarbageResponseBody = resolveJsonStr(returnJsonStr);
		return null;
	}
	private CwGarbageResponseBody resolveJsonStr(String returnJsonStr) {
		if(ObjNullUtil.noEmptyOrNull(returnJsonStr)) {
			CwGarbageResponseBody cwGarbageResponseBody = JSON_RESOLVER.fromJson(returnJsonStr, CwGarbageResponseBody.class);
			
		}
		return null;
	}
}
