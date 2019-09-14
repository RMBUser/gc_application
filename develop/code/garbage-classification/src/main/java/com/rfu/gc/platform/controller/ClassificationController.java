package com.rfu.gc.platform.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rfu.gc.platform.entity.Category;
import com.rfu.gc.platform.entity.Garbage;
import com.rfu.gc.platform.service.GCService;
import com.rfu.gc.platform.service.LocalGCService;

@RestController
@RequestMapping(value="/classify")
public class ClassificationController {
	
	@Resource
	private LocalGCService LocalGCService;
	
	@Resource
	private GCService gCService;
	
	@GetMapping(value="/byStr")
	public List<Category> classifyByStr(String type) {
		return LocalGCService.queryGCByStr(type);
	}
	
	@GetMapping(value="/byGarbageName")
	public List<Garbage> classifyByStr1(String name) {
		return gCService.queryGCByStr(name);
	}
//	@GetMapping(value="/byId")
//	public List<Category> classifyById(Integer categoryId) {
//		return LocalGCService.queryGCById(categoryId);
//	}
}
