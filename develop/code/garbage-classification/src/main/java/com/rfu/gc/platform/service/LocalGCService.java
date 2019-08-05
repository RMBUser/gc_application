package com.rfu.gc.platform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rfu.gc.platform.dao.CategoryRepository;
import com.rfu.gc.platform.entity.Category;

@Service
public class LocalGCService {

	@Autowired
	CategoryRepository categoryRepository;
	
	public List<Category> queryGCByStr(String categoryType) {
		return categoryRepository.findByTypeLike(categoryType);
	}
}
