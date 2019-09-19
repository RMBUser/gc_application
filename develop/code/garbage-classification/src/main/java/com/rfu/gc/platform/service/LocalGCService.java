package com.rfu.gc.platform.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rfu.gc.platform.dao.GarbageClassificationRepository;
import com.rfu.gc.platform.dao.GarbageRepository;
import com.rfu.gc.platform.entity.Garbage;
import com.rfu.gc.platform.entity.GarbageClassification;
import com.rfu.gc.platform.entity.TypeOfGarbage;
import com.rfu.gc.platform.pub.util.ObjNullUtil;
import com.rfu.gc.platform.pub.util.ReflectUtil;

@Service
public class LocalGCService {

	@Autowired
	private GarbageRepository garbageRepository;
	@Autowired
	private GarbageClassificationRepository garbageClassificationRepository;

	public List<TypeOfGarbage> queryGCByStr(final String garbageName) {
		if (ObjNullUtil.emptyOrNull(garbageName))
			return null;
		final List<TypeOfGarbage> togList = new ArrayList<TypeOfGarbage>();
		char[] keys = garbageName.toCharArray();
		final StringBuilder garbageNamebuilder = new StringBuilder();
		for (int i = 0; i < keys.length; i++) {
			garbageNamebuilder.append("%").append(keys[i]);
		}
		garbageNamebuilder.append("%");
		try {
			List<Garbage> garbagelist = garbageRepository.findByGarbageNameLike(garbageNamebuilder.toString());
			if (ObjNullUtil.noEmptyOrNull(garbagelist)) {
				Garbage[] garbages = garbagelist.toArray(new Garbage[0]);
				List<GarbageClassification> gcList = garbageClassificationRepository.findByGarbageIn(garbages);
				gcList.forEach(g -> {
					TypeOfGarbage tfb = ReflectUtil.copyU2T(g.getGarbage(), TypeOfGarbage.class, true);
					ReflectUtil.copyU2T(g.getCategory(), tfb, true);
					togList.add(tfb);
				});
				togList.sort((o1, o2) -> {
					if (garbageName.equals(o1.getGarbageName())) {
						if (null == o1.getIsFullMatch())
							o1.setIsFullMatch(true);
						return -1;
					} else if (garbageName.equals(o2.getGarbageName())) {
						if (null == o2.getIsFullMatch())
							o2.setIsFullMatch(true);
						return 1;
					} else {
						if (null == o1.getIsFullMatch())
							o1.setIsFullMatch(false);
						if (null == o2.getIsFullMatch())
							o2.setIsFullMatch(false);
						return o1.getGarbageName().compareTo(o2.getGarbageName());
					}
				});
			}
		} catch (Exception e) {
			//上面代码并没有显式的异常，try-catch一下主要是为了出现runnable等异常能有返回值
			e.printStackTrace();
		}
		return togList;
	}

}
