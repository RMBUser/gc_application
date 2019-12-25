package com.rfu.gc.platform.service.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.rfu.gc.platform.dao.GarbageClassificationRepository;
import com.rfu.gc.platform.dao.GarbageRepository;
import com.rfu.gc.platform.entity.Category;
import com.rfu.gc.platform.entity.Garbage;
import com.rfu.gc.platform.entity.GarbageClassification;
import com.rfu.gc.platform.pub.util.ObjNullUtil;

@Service
@CacheConfig(cacheManager = "ehcacheManager")
public class EhcacheService {
	@Resource
	private CacheManager ehcacheManager;
	@Autowired
	private GarbageRepository garbageRepository;
	@Autowired
	private GarbageClassificationRepository garbageClassificationRepository;

	public CacheManager getCacheManager() {
		return ehcacheManager;
	}

	@Cacheable(value = "garbageCache", key = "#p0", unless = "#result == null||#result.isEmpty()")
	public List<Garbage> getGarbagesByName(String garbageName) {
		return garbageRepository.findByGarbageName(garbageName);
	}

	@Cacheable(value = "garbageCache", key = "'unique'+#p0", unless = "#result == null")
	public Garbage getUniqueGarbageByName(String garbageName) throws NoUniqueEntityException {
		if (ObjNullUtil.noEmptyOrNull(garbageName)) {
			List<Garbage> garbages = null;
			if (ObjNullUtil.noEmptyOrNull(garbages = garbageRepository.findByGarbageName(garbageName))) {
				if (1 == garbages.size()) {
					return garbages.get(0);
				} else {
					throw new NoUniqueEntityException(garbages);
				}
			}
		}
		return new Garbage();
	}

	@Cacheable(value = "garbageCache", key = "#root.args[0]", sync = true)
	public Optional<Garbage> getUniqueGarbage(Integer garbageId) {
		return garbageRepository.findById(garbageId);
	}

	@Cacheable(value = "garbageCache", key = "'all'", sync = true)
	public List<Garbage> getAllGarbages() {
		return garbageRepository.findAll();
	}

	@Cacheable(value = "gcCache", key = "'all'", sync = true)
	public List<GarbageClassification> getAllGC() {
		return garbageClassificationRepository.findAll();
	}

	@Cacheable(value = "gcCache", key = "root.methodName+#a0", unless = "#result.isPresent()")
	public Optional<GarbageClassification> getUniqueGC(Integer gcId) {
		return garbageClassificationRepository.findById(gcId);
	}

	@Caching(put = {
			@CachePut(value = "garbageCache", key = "'unique_'+#result.garbageName", unless = "#result == null"),
			@CachePut(value = "garbageCache", key = "#result.garbageId", unless = "#result == null") }, evict = @CacheEvict(cacheNames = "garbageCache", key = "'all'"))
	public Garbage insertNewGarbage(String garbageName, String desc, Short origin, String originAdr) {
		return garbageRepository.insertNewOne(garbageName, desc, origin, originAdr);
	}

	@Caching(put = @CachePut(cacheNames = "gcCache", key = "#result.gcId", unless = "#result == null"), evict = @CacheEvict(value = "gcCache", key = "'all'"))
	public GarbageClassification insertNewGC(Garbage garbage, Category category, Short origin, String originAdr) {
		return garbageClassificationRepository.insertNewOne(garbage, category, origin, originAdr);
	}

	public List<GarbageClassification> getGCByGarbageIdOrName(Garbage garbage) {
		if (garbage != null) {
			if (garbage.getGarbageId() != null) {
				return getGCByGarbageId(garbage);
			} else {
				return getGCByGarbageName(garbage.getGarbageName());
			}
		}
		return new ArrayList<>();
	}

	@Cacheable(cacheNames = "gcCache", key = "'garbage_'+#garbage.garbageId", unless = "#result==null||#result.isEmpty()")
	public List<GarbageClassification> getGCByGarbageId(Garbage garbage) {
		return garbageClassificationRepository.findByGarbage(garbage);
	}

	@Cacheable(cacheNames = "gcCache", key = "'garbage_'+#garbageName", unless = "#result==null||#result.isEmpty()")
	public List<GarbageClassification> getGCByGarbageName(String garbageName) {
		if (ObjNullUtil.noEmptyOrNull(garbageName)) {
			GarbageClassification gcCondition = new GarbageClassification();
			Garbage g = new Garbage();
			g.setGarbageName(garbageName);
			gcCondition.setGarbage(g);
			Example<GarbageClassification> gcExample = Example.of(gcCondition);
			return garbageClassificationRepository.findAll(gcExample);
		} else
			return new ArrayList<>();
	}
}
