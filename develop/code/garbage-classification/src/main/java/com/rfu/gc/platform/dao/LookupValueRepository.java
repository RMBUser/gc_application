package com.rfu.gc.platform.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rfu.gc.platform.entity.LookupValue;

public interface LookupValueRepository extends JpaRepository<LookupValue, Integer> {
	List<LookupValue> findByLookupTypeCode(String lookupTypeCode);
	LookupValue findOneByLookupTypeCodeAndLookupKey(String lookupTypeCode, String lookupKey);
}
