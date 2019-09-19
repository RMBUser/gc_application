package com.rfu.gc.platform.service.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.google.gson.Gson;
import com.rfu.gc.platform.dao.CategoryRepository;
import com.rfu.gc.platform.entity.Category;
import com.rfu.gc.platform.entity.Garbage;
import com.rfu.gc.platform.entity.GarbageClassification;
import com.rfu.gc.platform.entity.RemoteCallResultWapper;
import com.rfu.gc.platform.entity.TypeOfGarbage;
import com.rfu.gc.platform.entity.lr3800.LajiInfo;
import com.rfu.gc.platform.entity.lr3800.LajiResponseBody;
import com.rfu.gc.platform.pub.util.AsyncLogbackHelper;
import com.rfu.gc.platform.pub.util.ObjNullUtil;
import com.rfu.gc.platform.pub.util.ReflectUtil;
import com.rfu.gc.platform.service.util.ApiReq;

@Service
@PropertySource(value = { "classpath:apis.properties" })
public class Lr3800Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(Lr3800Service.class);

	@Qualifier("httpsApiReq")
	@Autowired
	private ApiReq apiReq;
	@Autowired
	private CategoryRepository categoryRepository;
	@Value(value = "${apis.gc.lr3800.apiAdress}")
	private String apiAdress;
	@Autowired
	private AsyncLogbackHelper asyncLogbackHelper;

	private static final Gson JSON_RESOLVER;

	static {
		JSON_RESOLVER = new Gson();
	}

	public RemoteCallResultWapper<List<TypeOfGarbage>> getTypeOfGarbage(String garbageName) {
		if (ObjNullUtil.noEmptyOrNull(garbageName)) {
			LajiResponseBody lajiResponseBody = null;
			try {
				lajiResponseBody = callLr3800Api(garbageName);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			if (lajiResponseBody != null && lajiResponseBody.getCode() != null) {
				RemoteCallResultWapper<List<TypeOfGarbage>> result = new RemoteCallResultWapper<>();
				if (200 == lajiResponseBody.getCode()) {
					List<TypeOfGarbage> togList = new ArrayList<TypeOfGarbage>();
					result.setTarget(togList);
					List<LajiInfo> lajiInfoList = lajiResponseBody.getNewslist();
					if (ObjNullUtil.noEmptyOrNull(lajiInfoList)) {
						List<GarbageClassification> gcList = new ArrayList<GarbageClassification>();
						result.setGarbageClassificationList(gcList);
						for (LajiInfo lajiInfo : lajiInfoList) {
							Optional<Category> oc = categoryRepository.findById(lajiInfo.getType() + 1);
							if (oc.isPresent()) {
								Category category = oc.get();
								TypeOfGarbage tfb = ReflectUtil.copyU2T(category, TypeOfGarbage.class, true);
								tfb.setGarbageName(lajiInfo.getName());
								if (garbageName.equals(lajiInfo.getName())) {
									tfb.setIsFullMatch(true);
									togList.add(0, tfb);
								} else {
									tfb.setIsFullMatch(false);
									togList.add(tfb);
								}
								// 顺便封装gcList
								GarbageClassification gc = new GarbageClassification();
								Garbage garbage = new Garbage();
								garbage.setGarbageName(lajiInfo.getName());
								gc.setCategory(category);
								gc.setGarbage(garbage);
								gcList.add(gc);
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
				} else {
					asyncLogbackHelper.warn(LOGGER,
							"(⊙﹏⊙) getTypeOfGarbage==>Calling api:" + getApiAdr() + "?name=" + garbageName
									+ " fail==>Return code:" + lajiResponseBody.getCode() + ";Return message:"
									+ lajiResponseBody.getMsg());
//					LOGGER.warn("(⊙﹏⊙) getTypeOfGarbage==>Calling api:" + getApiAdr() + "?name=" + garbageName
//							+ " fail==>Return code:" + lajiResponseBody.getCode() + ";Return message:"
//							+ lajiResponseBody.getMsg());
				}
				return result;
			}
		}
		return null;
	}

	public String getApiAdr() {
		return this.apiAdress;
	}

	private LajiResponseBody callLr3800Api(String garbageName) throws IOException {
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put("name", garbageName);
		String responseStr = apiReq.sendReq(getApiAdr(), argsMap, ApiReq.GET);
		if (ObjNullUtil.noEmptyOrNull(responseStr)) {
			return JSON_RESOLVER.fromJson(responseStr, LajiResponseBody.class);
		}
		return null;
	}
}
