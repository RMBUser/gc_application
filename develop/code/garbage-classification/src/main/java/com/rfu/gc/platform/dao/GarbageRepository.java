package com.rfu.gc.platform.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.rfu.gc.platform.entity.Garbage;

public interface GarbageRepository extends JpaRepository<Garbage, Integer> {
	
	 List<Garbage> findByGarbageName(String garbageName);
	
	
	@Query(value = "SELECT t FROM Garbage t WHERE t.garbageName LIKE %?1%")
	public List<Garbage> findByGarbageLike(String garbageName);
	
//	@Query(value = "SELECT t FROM Category t WHERE t. = ?1")
//	public List<Category> findByCategoryIdLike(int categoryId);
//	
	
	
	
}

