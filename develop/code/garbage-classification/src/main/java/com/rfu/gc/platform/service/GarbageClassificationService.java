package com.rfu.gc.platform.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.rfu.gc.platform.dao.GarbageClassificationRepository;
import com.rfu.gc.platform.entity.Garbage;
import com.rfu.gc.platform.entity.GarbageClassification;
import com.rfu.gc.platform.pub.util.ObjNullUtil;

@Service
public class GarbageClassificationService {

	@Autowired
	private GarbageClassificationRepository garbageClassificationRepository;

	public List<GarbageClassification> queryGCByGarbageIdOrName(Garbage garbage) {
		if (garbage.getGarbageId() != null) {
			return garbageClassificationRepository.findByGarbage(garbage);
		} else if (ObjNullUtil.noEmptyOrNull(garbage.getGarbageName())) {
			GarbageClassification gcCondition = new GarbageClassification();
			Garbage g = new Garbage();
			g.setGarbageName(garbage.getGarbageName());
			gcCondition.setGarbage(g);
			Example<GarbageClassification> gcExample = Example.of(gcCondition);
			return garbageClassificationRepository.findAll(gcExample);
		} else
			return new ArrayList<>();
	}
}
