package com.rfu.gc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.rfu.gc.entity.CategoryCopy;
import com.rfu.gc.platform.entity.Category;
import com.rfu.gc.platform.entity.lr3800.LajiInfo;
import com.rfu.gc.platform.entity.lr3800.LajiResponseBody;
import com.rfu.gc.platform.pub.util.ReflectUtil;
import com.rfu.gc.platform.service.util.ApiReq;
import com.rfu.gc.platform.service.util.HttpApiReq;
import com.rfu.gc.platform.service.util.HttpsApiReq;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GarbageClassificationApplicationTests {

	@Resource
	HttpsApiReq httpsApiReq;
	@Test
	public void contextLoads() {
		
	}
	
	public String testApiReq01() {
		Map<String, String> args = new HashMap<String, String>();
		args.put("name", "苹果");
		String urlString = "";
		try {
			urlString = httpsApiReq.sendReq("https://laji.lr3800.com/api.php", args, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(urlString);
		return urlString;
	}
	
	@Test
	public void testReflect01() {
		Gson gson = new Gson();
		LajiResponseBody lajiResponseBody = gson.fromJson(testApiReq01(), LajiResponseBody.class);
		List<LajiInfo> lajiInfoList = lajiResponseBody.getNewslist();
		lajiInfoList.forEach(GarbageClassificationApplicationTests::testReflect01);
	}
	
	public static String testApiReq() {
		HttpsApiReq httpApiReq = new HttpsApiReq();
		Map arg = new HashMap();
		arg.put("name", "纸巾");
		String urlString = "";
		try {
			urlString = httpApiReq.sendReq("https://laji.lr3800.com/api.php", arg, null);
//			urlString = httpApiReq.sendReq("http://localhost:8080/classify/byStr", arg, ApiReq.POST);
//			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//			List list = gson.fromJson(urlString, List.class);
//			LinkedTreeMap ltm = (LinkedTreeMap)list.get(0);
//			String first = gson.toJson(ltm);
//			Category category = gson.fromJson(first, Category.class);
//			category.setIsEnable(0);
//			System.out.println(gson.toJson(category, category.getClass()));
//			System.out.println(gson.toJson(list, list.getClass()));
//			arg.put("list", list);
//			arg.put("entity", category);
//			System.out.println(gson.toJson(arg, arg.getClass()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(urlString);
		return urlString;
	}
	
	public static <U> void testReflect01(U u) {
		Category category = ReflectUtil.copyU2T(u, Category.class, true);
		System.out.println(category.getCategoryType());
		System.out.println(category.getOriginAdr());
//			System.out.println(category.getCategoryId());
	}
	
	public static <U> void testReflect02() {
		CategoryCopy categoryCopy = new CategoryCopy();
		categoryCopy.setCategoryId(55l);
		categoryCopy.setCategoryType("Raymond");
		categoryCopy.setOrigin(4);
		categoryCopy.setOriginAdr("Nicole");
		categoryCopy.setIsEnable(0);
		Category category = new Category();
		ReflectUtil.copyU2T(categoryCopy, category, true);
		System.out.println(category.getCategoryId());
		System.out.println(category.getCategoryType());
		System.out.println(category.getOrigin());
		System.out.println(category.getOriginAdr());
		System.out.println(category.getIsEnable());
	}
	
	public static void testReflect03() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("categoryId", 33l);
		map.put("categoryType", "可回收LJ");
		map.put("createdTime", new Date());
		map.put("isEnable", null);
		Map<String, Object> lajiMap = new HashMap<String, Object>();
		lajiMap.put("name", "Airpods");
		lajiMap.put("type", 3);
		lajiMap.put("raymond", "raymond");
		map.put("lajiInfo", lajiMap);
		CategoryCopy categoryCopy = new CategoryCopy();
		categoryCopy.setIsEnable(0);
		ReflectUtil.map2Entity(map, categoryCopy);
//		CategoryCopy categoryCopy = ReflectUtil.map2Entity(map, CategoryCopy.class);
		System.out.println(categoryCopy.getCategoryId());
		System.out.println(categoryCopy.getCategoryType());
		System.out.println(categoryCopy.getCreatedTime());
		System.out.println(categoryCopy.getIsEnable());
		System.out.println(categoryCopy.lajiInfo.getName());
		System.out.println(categoryCopy.lajiInfo.getType());
		Map<String, Object> entityMap = ReflectUtil.entity2Map(categoryCopy, null, true);
		entityMap.forEach((k,v)->System.out.println("key:"+k+"="+v));
		System.out.println(entityMap.containsKey("categoryType"));
		LajiInfo lj = (LajiInfo)entityMap.get("lajiInfo");
		System.out.println(lj.getName()+lj.getType());
	}

	public static void main(String[] args) throws InstantiationException {
		testReflect03();
	}
}
