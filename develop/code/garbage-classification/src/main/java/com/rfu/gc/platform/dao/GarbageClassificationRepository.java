package com.rfu.gc.platform.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rfu.gc.platform.entity.GarbageClassification;
import com.rfu.gc.platform.entity.Category;
import com.rfu.gc.platform.entity.Garbage;

import java.util.Date;
import java.util.List;

public interface GarbageClassificationRepository extends JpaRepository<GarbageClassification, Integer> {

	List<GarbageClassification> findByGarbageIn(Garbage[] garbages);

	List<GarbageClassification> findByGarbage(Garbage garbage);

	default GarbageClassification insertNewOne(Garbage garbage, Category category, Short origin, String originAdr) {
		GarbageClassification entity = new GarbageClassification();
		entity.setCategory(category);
		entity.setGarbage(garbage);
		entity.setOrigin(origin);
		entity.setOriginAdr(originAdr);
		entity.setIsEnable(new Short("1"));
		entity.setCreatedTime(new Date());
		return save(entity);
	}
}
