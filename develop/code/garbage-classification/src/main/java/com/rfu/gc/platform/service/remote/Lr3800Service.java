package com.rfu.gc.platform.service.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.rfu.gc.platform.entity.Category;
import com.rfu.gc.platform.entity.TypeOfGarbage;
import com.rfu.gc.platform.entity.lr3800.LajiInfo;
import com.rfu.gc.platform.entity.lr3800.LajiResponseBody;
import com.rfu.gc.platform.pub.util.AsyncLogbackHelper;
import com.rfu.gc.platform.pub.util.ObjNullUtil;
import com.rfu.gc.platform.pub.util.ReflectUtil;
import com.rfu.gc.platform.service.pub.DefaultCacheService;
import com.rfu.gc.platform.service.util.ApiReq;

@Service
@PropertySource(value = { "classpath:apis.properties" })
public class Lr3800Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(Lr3800Service.class);

	@Qualifier("httpsApiReq")
	@Autowired
	private ApiReq apiReq;
	@Autowired
	private DefaultCacheService defaultCacheService;
	@Value(value = "${apis.gc.lr3800.apiAdress}")
	private String apiAdress;
	@Autowired
	private AsyncLogbackHelper asyncLogbackHelper;

	private static final Gson JSON_RESOLVER;

	static {
		JSON_RESOLVER = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	}

	@Async("crisExecutor")
	public Future<List<TypeOfGarbage>> getTypeOfGarbage(String garbageName) {
		List<TypeOfGarbage> result = new ArrayList<>();
		if (ObjNullUtil.noEmptyOrNull(garbageName)) {
			LajiResponseBody lajiResponseBody = null;
			try {
				lajiResponseBody = callLr3800Api(garbageName);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			if (lajiResponseBody != null && lajiResponseBody.getCode() != null) {
				if (new Integer(200).equals(lajiResponseBody.getCode())) {
					List<LajiInfo> lajiInfoList = lajiResponseBody.getNewslist();
					if (ObjNullUtil.noEmptyOrNull(lajiInfoList)) {
						for (LajiInfo lajiInfo : lajiInfoList) {
							Optional<Category> oc = defaultCacheService.getUniqueCategory(lajiInfo.getType() + 1);
							if (oc.isPresent()) {
								Category category = oc.get();
								TypeOfGarbage tfb = ReflectUtil.copyU2T(category, TypeOfGarbage.class, true);
								tfb.setGarbageName(lajiInfo.getName());
								tfb.setOriginAdr(getApiAdr());
								if (garbageName.equals(lajiInfo.getName())) {
									tfb.setIsFullMatch(true);
									result.add(0, tfb);
								} else {
									tfb.setIsFullMatch(false);
									result.add(tfb);
								}
							} else {
								asyncLogbackHelper.warn(LOGGER,
										"(⊙﹏⊙) getTypeOfGarbage==>No suitable matching type from local categories was found to match the entity info from "
												+ getApiAdr() + "?name=" + garbageName + "==><" + lajiInfo.toString()
												+ ">.");
//								LOGGER.warn(
//										"(⊙﹏⊙) getTypeOfGarbage==>No suitable matching type from local categories was found to match the entity info from "
//												+ getApiAdr() + "?name=" + garbageName + "==><" + lajiInfo.toString()
//												+ ">.");
							}
						}
					}
				} else if (new Integer(250).equals(lajiResponseBody.getCode())) {
					// 250是没数据 没什么操作
				} else {
					asyncLogbackHelper.warn(LOGGER,
							"(⊙﹏⊙) getTypeOfGarbage==>Calling api:" + getApiAdr() + "?name=" + garbageName
									+ " fail==>Return code:" + lajiResponseBody.getCode() + ";Return message:"
									+ lajiResponseBody.getMsg());
				}
			}
		}
//		else {
//		异步处理 抛异常不太好
//			throw new IllegalArgumentException();
//		}
		return new AsyncResult<List<TypeOfGarbage>>(result);
	}

	public String getApiAdr() {
		return this.apiAdress;
	}

	private LajiResponseBody callLr3800Api(String garbageName) throws IOException {
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put("name", garbageName);
		String responseStr = apiReq.sendReq(getApiAdr(), argsMap, ApiReq.GET);
		if (ObjNullUtil.noEmptyOrNull(responseStr)) {
			try {
				return JSON_RESOLVER.fromJson(responseStr, LajiResponseBody.class);
			} catch (JsonSyntaxException e) {
				asyncLogbackHelper.error(LOGGER, "calls " + apiAdress + " fail:the json string(" + responseStr
						+ ") from " + apiAdress + " is unresolved ");
				e.printStackTrace();
			}
		}
		return null;
	}
}
