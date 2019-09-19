package com.rfu.gc.platform.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rfu.gc.platform.entity.Garbage;
import java.lang.String;

public interface GarbageRepository extends JpaRepository<Garbage, Integer> {

	List<Garbage> findByGarbageNameLike(String garbagename);

	List<Garbage> findByGarbageName(String garbageName);

	default Garbage insertNewOne(String garbageName, String desc, Short origin, String originAdr) {
		Garbage entity = new Garbage();
		entity.setGarbageName(garbageName);
		entity.setGarbageDesc(desc);
		entity.setOrigin(origin);
		entity.setOriginAdr(originAdr);
		entity.setIsEnable(new Short("1"));
		entity.setCreatedTime(new Date());
		return save(entity);
	}
}
