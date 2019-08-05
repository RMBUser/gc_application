package com.rfu.gc.platform.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rfu.gc.platform.entity.Category;
import com.rfu.gc.platform.service.LocalGCService;

@RestController
@RequestMapping(value="/classify")
public class ClassificationController {
	
	@Resource
	LocalGCService LocalGCService;
	
	@GetMapping(value="/byStr")
	public List<Category> classifyByStr(String type) {
		return LocalGCService.queryGCByStr(type);
	}
}
