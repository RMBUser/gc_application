package com.rfu.gc.platform.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.rfu.gc.platform.dao.CategoryRepository;
import com.rfu.gc.platform.dao.GarbageClassificationRepository;
import com.rfu.gc.platform.dao.GarbageRepository;
import com.rfu.gc.platform.entity.Category;
import com.rfu.gc.platform.entity.Garbage;
import com.rfu.gc.platform.entity.GarbageClassification;
import com.rfu.gc.platform.entity.RemoteCallResultWapper;
import com.rfu.gc.platform.entity.ResponseGCModel;
import com.rfu.gc.platform.entity.TypeOfGarbage;
import com.rfu.gc.platform.pub.util.ObjNullUtil;
import com.rfu.gc.platform.service.remote.Lr3800Service;

@Service
public class RemoteGCServive {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteGCServive.class);

	@Autowired
	private Lr3800Service lr3800Service;
	@Autowired
	private GarbageRepository garbageRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private GarbageClassificationRepository garbageClassificationRepository;

	@Async("crisExecutor")
	public Future<ResponseGCModel<List<TypeOfGarbage>>> ask4ResultFromLr3800(String garbageName) {
		synchronized (garbageName) {
			ResponseGCModel<List<TypeOfGarbage>> responseGCModel = new ResponseGCModel<>();
			responseGCModel.setRetCode(ResponseGCModel.UNKNOW_ERROR_CODE);
			responseGCModel.setRetMsg(ResponseGCModel.UNKNOW_ERROR_MSG);
			responseGCModel.setData(Arrays.asList(new TypeOfGarbage[0]));
			try {
				RemoteCallResultWapper<List<TypeOfGarbage>> callResult = lr3800Service.getTypeOfGarbage(garbageName);
				if (callResult != null) {
					if (callResult.getTarget() != null) {
						responseGCModel.setData(callResult.getTarget());
						if (!callResult.getTarget().isEmpty()) {
							responseGCModel.setRetCode(ResponseGCModel.SUCCESS);
							responseGCModel.setRetMsg(ResponseGCModel.SUCCESS_MSG);
							if (ObjNullUtil.noEmptyOrNull(callResult.getGarbageClassificationList()))
								asyncUpdateLocaldb(callResult.getGarbageClassificationList());
						} else {
							responseGCModel.setRetCode(ResponseGCModel.DATA_NOT_FOUND_CODE);
							responseGCModel.setRetMsg(ResponseGCModel.DATA_NOT_FOUND_MSG);
						}
					} else {
						responseGCModel.setRetCode(ResponseGCModel.API_CALL_FAIL_CODE);
						responseGCModel.setRetMsg(ResponseGCModel.DATA_NOT_FOUND_MSG);
					}
				}
			} catch (Exception e) {
				// 上面代码并没有显式的异常，try-catch一下主要是为了出现runnable等异常能有返回值
				e.printStackTrace();
			}
			garbageName.notify();
			return new AsyncResult<ResponseGCModel<List<TypeOfGarbage>>>(responseGCModel);
		}
	}

	@Async("crisExecutor")
	public void asyncUpdateLocaldb(List<GarbageClassification> gcList) {
		if (ObjNullUtil.noEmptyOrNull(gcList)) {
			gcList.forEach(this::updateTask);
		}
	}

	@Transactional
	private void updateTask(GarbageClassification garbageClassification) {
		if (garbageClassification != null && garbageClassification.getCategory() != null
				&& garbageClassification.getGarbage() != null) {
			Category sourceCategory = garbageClassification.getCategory();
			Garbage sourceGarbage = garbageClassification.getGarbage();
			List<Category> candidateCategoryList = null;
			List<Garbage> candidateGarbageList = null;
			// 先检查分类是因为，分类ID必须有保障，该分类必须在我们库里有
			if (sourceCategory.getCategoryId() == null) {
				if (ObjNullUtil.noEmptyOrNull(sourceCategory.getCategoryType()))
					candidateCategoryList = categoryRepository.findByCategoryType(sourceCategory.getCategoryType());
			} else {
				candidateCategoryList = new ArrayList<Category>();
				candidateCategoryList.add(sourceCategory);
			}
			// 若size=0,即出现新的分类类型，则先不更新，从长计议；若size>1,则数据库分类类型有重名问题
			if (candidateCategoryList != null && candidateCategoryList.size() == 1) {
				GarbageClassification savedGC = null;
				Example<GarbageClassification> gcExample = Example.of(garbageClassification);
				List<GarbageClassification> candidateGCList = garbageClassificationRepository.findAll(gcExample);
				if (ObjNullUtil.emptyOrNull(candidateGCList)) {
					if (sourceGarbage.getGarbageId() == null) {
						if (ObjNullUtil.noEmptyOrNull(sourceGarbage.getGarbageName()))
							candidateGarbageList = garbageRepository.findByGarbageName(sourceGarbage.getGarbageName());
					} else {
						candidateGarbageList = new ArrayList<Garbage>();
						candidateGarbageList.add(sourceGarbage);
					}
					if (ObjNullUtil.noEmptyOrNull(candidateGarbageList)) {
						// 即使是同名垃圾，加一个就够了
						savedGC = garbageClassificationRepository.insertNewOne(candidateGarbageList.get(0),
								sourceCategory, new Short("2"), lr3800Service.getApiAdr());

					} else {
						if(ObjNullUtil.noEmptyOrNull(sourceGarbage.getGarbageName())) {
							Garbage newGarbage = garbageRepository.insertNewOne(sourceGarbage.getGarbageName(), sourceGarbage.getGarbageDesc(),
									new Short("2"), lr3800Service.getApiAdr());
							savedGC = garbageClassificationRepository.insertNewOne(newGarbage, sourceCategory,
									new Short("2"), lr3800Service.getApiAdr());
						}
					}
//						boolean needUpdate = true;
//						List<GarbageClassification> mayNeedUpdateGCList = new ArrayList<GarbageClassification>();
//						for (GarbageClassification candidateGC : candidateGCList) {
//							Integer matchType = candidateGC.getCategory().getCategoryId();
//							//即使同名垃圾，其中一个有这个gc记录都不需要插入或更新
//							if (candidateCategoryList.get(0).getCategoryId() == null || candidateCategoryList.get(0).getCategoryId() == matchType||!mayNeedUpdateGCList.add(candidateGC))
//								needUpdate = false;
//						}
//						if (!needUpdate) return;
//						if (mayNeedUpdateGCList.size() == 1) {
//							//todo记录日志可能需要update
//							return;
//						}
					// 即使是同名垃圾，加一个就够了
//						savedGC = garbageClassificationRepository.insertNewOne(oldGarbageList.get(0), cOptional.get(), origin, apiAdress);
					if (savedGC != null && savedGC.getGcId() != null) {
						LOGGER.info("o(*￣▽￣*)o updateTask==>Update new entity of <" + garbageClassification.toString()
						+ "> into local database success.");
					} else {
						LOGGER.error("(╯▔皿▔)╯ updateTask==>Update new entity of <" + garbageClassification.toString()
						+ "> into local database fail.");
					}
				}
			} else {
				if (ObjNullUtil.emptyOrNull(candidateCategoryList))
					LOGGER.warn("(⊙﹏⊙) updateTask==>Please detect if <" + garbageClassification.toString()
							+ "> should be allow to update into local database on the ground that its category didnt found a suitable match.");
				if (candidateCategoryList.size() > 1)
					LOGGER.error(
							"(╯▔皿▔)╯ updateTask==>Polytype of same name was found.Please detect the data of Category table.");
			}
		}
	}
}
