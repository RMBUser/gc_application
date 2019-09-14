package com.rfu.gc.platform.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rfu.gc.platform.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	 List<Category> findByCategoryType(String categoryType);
	
	
	@Query(value = "SELECT t FROM Category t WHERE t.categoryType LIKE %?1%")
	public List<Category> findByTypeLike(String categoryType);
	
	
	
	
	
	
}
