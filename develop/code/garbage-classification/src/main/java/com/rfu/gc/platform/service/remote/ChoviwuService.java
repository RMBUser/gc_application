package com.rfu.gc.platform.service.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.rfu.gc.platform.entity.Category;
import com.rfu.gc.platform.entity.LookupValue;
import com.rfu.gc.platform.entity.TypeOfGarbage;
import com.rfu.gc.platform.entity.choviwu.CwGarbageInfo;
import com.rfu.gc.platform.entity.choviwu.CwGarbageResponseBody;
import com.rfu.gc.platform.pub.util.AsyncLogbackHelper;
import com.rfu.gc.platform.pub.util.ObjNullUtil;
import com.rfu.gc.platform.pub.util.ReflectUtil;
import com.rfu.gc.platform.service.pub.DefaultCacheService;
import com.rfu.gc.platform.service.util.ApiReq;
import com.rfu.gc.platform.service.util.HttpApiReq;

@Service
@PropertySource("classpath:apis.properties")
public class ChoviwuService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChoviwuService.class);
	@Autowired
	private HttpApiReq httpApiReq;
	@Value("${apis.gc.choviwu.apiAdress}")
	private String apiAdress;
	@Value("${apis.gc.lookup-type}")
	private String lookupTypeCode;
	@Autowired
	private AsyncLogbackHelper asyncLogbackHelper;
	@Autowired
	private DefaultCacheService defaultCacheService;
	private static final Gson JSON_RESOLVER;

	static {
		JSON_RESOLVER = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	}

	@Async("crisExecutor")
	public Future<List<TypeOfGarbage>> getTypeOfGarbage(String garbageName) {
		List<TypeOfGarbage> result = new ArrayList<>();
		CwGarbageResponseBody cwGarbageResponseBody = callHttpApi(garbageName);
		if (cwGarbageResponseBody != null && cwGarbageResponseBody.getCode() != null) {
			if (new Integer(200).equals(cwGarbageResponseBody.getCode())) {
				List<CwGarbageInfo> cwGarbageInfoList = cwGarbageResponseBody.getData();
				if (ObjNullUtil.noEmptyOrNull(cwGarbageInfoList)) {
					// 下面两个foreach的写法不会把fullmatch的推到第一位
					cwGarbageInfoList.forEach((c) -> result.add(ReflectUtil.copyU2T(c, TypeOfGarbage.class, true)));
					result.removeIf((t) -> {
						LookupValue lookupValue = defaultCacheService.getUniqueValue(lookupTypeCode,
								t.getCategoryType());
						if (lookupValue != null && lookupValue.getLookupValue() != null) {
							Optional<Category> oc = defaultCacheService
									.getUniqueCategory(Integer.parseInt(lookupValue.getLookupValue()));
							if (oc.isPresent()) {
								Category mappedCategory = oc.get();
								ReflectUtil.copyU2T(mappedCategory, t, true);
								t.setOriginAdr(apiAdress);
								if (garbageName.equals(t.getGarbageName()))
									t.setIsFullMatch(true);
								else
									t.setIsFullMatch(false);
								return false;
							} else {
								asyncLogbackHelper.warn(LOGGER,
										"(⊙﹏⊙) getTypeOfGarbage==>No suitable matching type from local categories was found to match the entity info from "
												+ apiAdress + "?garbageName=" + garbageName + "==><" + t.toString()
												+ ">.");
							}
						} else {
							asyncLogbackHelper.warn(LOGGER,
									"(⊙﹏⊙) getTypeOfGarbage==>No suitable matching type in lookupValue table was found to match the entity info from "
											+ apiAdress + "?garbageName=" + garbageName + "==><" + t.toString() + ">.");
						}
						return true;
					});
				}
			} else {
				asyncLogbackHelper.warn(LOGGER,
						"(⊙﹏⊙) getTypeOfGarbage==>Calling api:" + apiAdress + "?garbageName=" + garbageName
								+ " fail==>Return code:" + cwGarbageResponseBody.getCode() + ";Return message:"
								+ cwGarbageResponseBody.getMsg() + ";Return data:" + cwGarbageResponseBody.getData());
			}
		}
		return new AsyncResult<List<TypeOfGarbage>>(result);
	}

	public CwGarbageResponseBody callHttpApi(String garbageName) {
		Map<String, String> argsMap = new HashMap<>();
		argsMap.put("garbageName", garbageName);
		String returnJsonStr = null;
		try {
			returnJsonStr = httpApiReq.sendReq(apiAdress, argsMap, ApiReq.GET);
			return resolveJsonStr(returnJsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private CwGarbageResponseBody resolveJsonStr(String returnJsonStr) {
		if (ObjNullUtil.noEmptyOrNull(returnJsonStr)) {
			try {
				CwGarbageResponseBody cwGarbageResponseBody = JSON_RESOLVER.fromJson(returnJsonStr,
						CwGarbageResponseBody.class);
				return cwGarbageResponseBody;
			} catch (JsonSyntaxException e) {
				asyncLogbackHelper.error(LOGGER, "calls " + apiAdress + " fail:the json string(" + returnJsonStr
						+ ") from " + apiAdress + " is unresolved ");
				e.printStackTrace();
			}
		}
		return null;
	}
}
