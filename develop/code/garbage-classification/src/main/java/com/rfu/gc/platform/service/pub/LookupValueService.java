package com.rfu.gc.platform.service.pub;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rfu.gc.platform.dao.LookupValueRepository;
import com.rfu.gc.platform.entity.LookupValue;

@Service
public class LookupValueService {
	@Autowired
	private LookupValueRepository lookupValueRepository;
	
	public List<LookupValue> getValuesByTypeCode(String lookupTypeCode) {
		return lookupValueRepository.findByLookupTypeCode(lookupTypeCode);
	}

	public LookupValue getUniqueValue(String lookupTypeCode, String lookupKey) {
		return lookupValueRepository.findOneByLookupTypeCodeAndLookupKey(lookupTypeCode, lookupKey);
	}
}
