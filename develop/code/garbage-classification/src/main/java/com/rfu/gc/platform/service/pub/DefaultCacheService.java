package com.rfu.gc.platform.service.pub;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.rfu.gc.platform.dao.CategoryRepository;
import com.rfu.gc.platform.dao.LookupValueRepository;
import com.rfu.gc.platform.entity.Category;
import com.rfu.gc.platform.entity.LookupValue;

@Service
@CacheConfig(cacheManager = "defaultCacheManager")
public class DefaultCacheService {
	@Resource
	private CacheManager defaultCacheManager;
	@Autowired
	private LookupValueRepository lookupValueRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	
	public CacheManager getCacheManager() {
		return defaultCacheManager;
	}

	@Cacheable(cacheNames = "lookupValueCache", key = "#a0", unless = "#result == null")
	public List<LookupValue> getLKValuesByTypeCode(String lookupTypeCode) {
		return lookupValueRepository.findByLookupTypeCode(lookupTypeCode);
	}

	@Cacheable(cacheNames = "lookupValueCache", key = "#p0+'_'+#p1", unless = "#result == null")
	public LookupValue getUniqueValue(String lookupTypeCode, String lookupKey) {
		return lookupValueRepository.findOneByLookupTypeCodeAndLookupKey(lookupTypeCode, lookupKey);
	}

	@Cacheable(cacheNames = "lookupValueCache", key = "'all'", sync = true)
	public List<LookupValue> getAllLKValues() {
		return lookupValueRepository.findAll();
	}

	@Cacheable(value = "categoryCache", key = "#categoryType", unless = "#result == null")
	public List<Category> getCategoriesByType(String categoryType) {
		return categoryRepository.findByCategoryType(categoryType);
	}

	@Cacheable(value = "#categoryCache", sync = true)
	public Optional<Category> getUniqueCategory(Integer categoryId) {
		return categoryRepository.findById(categoryId);
	}

	@Cacheable(value = "categoryCache", key = "'all'", sync = true)
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}
}
