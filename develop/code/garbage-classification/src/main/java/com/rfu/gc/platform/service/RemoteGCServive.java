package com.rfu.gc.platform.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rfu.gc.platform.entity.Category;
import com.rfu.gc.platform.entity.Garbage;
import com.rfu.gc.platform.entity.GarbageClassification;
import com.rfu.gc.platform.entity.ResponseGCModel;
import com.rfu.gc.platform.entity.TypeOfGarbage;
import com.rfu.gc.platform.pub.util.ObjNullUtil;
import com.rfu.gc.platform.service.pub.DefaultCacheService;
import com.rfu.gc.platform.service.pub.EhcacheService;
import com.rfu.gc.platform.service.remote.ChoviwuService;
import com.rfu.gc.platform.service.remote.Lr3800Service;

@Service
public class RemoteGCServive {
	private final Logger LOGGER = LoggerFactory.getLogger(RemoteGCServive.class);
	@Autowired
	private Lr3800Service lr3800Service;
	@Autowired
	private ChoviwuService choviwuService;
	@Autowired
	private DefaultCacheService defaultCacheService;
	@Autowired
	private EhcacheService ehcacheService;

	@Async("crisExecutor")
	public Future<ResponseGCModel<List<TypeOfGarbage>>> ask4ResultFromLr3800(String garbageName) {
		ResponseGCModel<List<TypeOfGarbage>> responseGCModel = null;
		try {
			Future<List<TypeOfGarbage>> callChoviwuFuture = lr3800Service.getTypeOfGarbage(garbageName);
			Future<List<TypeOfGarbage>> callLr3800Future = choviwuService.getTypeOfGarbage(garbageName);
			Set<Future<List<TypeOfGarbage>>> callApisResultSet = new HashSet<>();
			callApisResultSet.add(callChoviwuFuture);
			callApisResultSet.add(callLr3800Future);
			responseGCModel = handleAsSoonAsPossible(callApisResultSet);
		} catch (Exception e) {
			// 上面代码并没有显式的异常，try-catch一下主要是为了出现runnable等异常能有返回值
			responseGCModel = new ResponseGCModel<>();
			responseGCModel.setRetCode(ResponseGCModel.UNKNOW_ERROR_CODE);
			responseGCModel.setRetMsg(ResponseGCModel.UNKNOW_ERROR_MSG);
			responseGCModel.setData(Arrays.asList(new TypeOfGarbage[0]));
			e.printStackTrace();
		}
		return new AsyncResult<ResponseGCModel<List<TypeOfGarbage>>>(responseGCModel);
	}

	private ResponseGCModel<List<TypeOfGarbage>> handleAsSoonAsPossible(
			Set<Future<List<TypeOfGarbage>>> candidateFutureSet) throws InterruptedException, ExecutionException {
		ResponseGCModel<List<TypeOfGarbage>> model = null;
		long startTime = System.currentTimeMillis();
		List<TypeOfGarbage> togList = null;
		while ((System.currentTimeMillis() - startTime) <= 3000) {
			if (ObjNullUtil.emptyOrNull(candidateFutureSet)) {
				return model;
			}
			for (Future<List<TypeOfGarbage>> future : candidateFutureSet) {
				if (future.isDone()) {
					System.out.println("one");
					System.out.println(future);
					togList = future.get();
					candidateFutureSet.remove(future);
					break;
				}
			}
			if (togList != null && ResponseGCModel.SUCCESS.equals((model = wrapResponseGCModel(togList)).getRetCode()))
				break;
		}
		if (model != null) {
			wait4candidateFuturesAndUpdateDb(candidateFutureSet, togList);
			return model;
		}
		model = new ResponseGCModel<>();
		model.setRetCode(ResponseGCModel.RESPONSE_TIME_OUT);
		model.setRetMsg(ResponseGCModel.RESPONSE_TIME_OUT_MSG);
		model.setData(Arrays.asList(new TypeOfGarbage[0]));
		return model;
	}

	private ResponseGCModel<List<TypeOfGarbage>> wrapResponseGCModel(List<TypeOfGarbage> callResult) {
		ResponseGCModel<List<TypeOfGarbage>> responseGCModel = new ResponseGCModel<>();
		responseGCModel.setRetCode(ResponseGCModel.UNKNOW_ERROR_CODE);
		responseGCModel.setRetMsg(ResponseGCModel.UNKNOW_ERROR_MSG);
		responseGCModel.setData(Arrays.asList(new TypeOfGarbage[0]));
		if (callResult != null) {
			responseGCModel.setData(callResult);
			if (!callResult.isEmpty()) {
				responseGCModel.setRetCode(ResponseGCModel.SUCCESS);
				responseGCModel.setRetMsg(ResponseGCModel.SUCCESS_MSG);
			} else {
				responseGCModel.setRetCode(ResponseGCModel.DATA_NOT_FOUND_CODE);
				responseGCModel.setRetMsg(ResponseGCModel.DATA_NOT_FOUND_MSG);
			}
		} else {
			responseGCModel.setRetCode(ResponseGCModel.API_CALL_FAIL_CODE);
			responseGCModel.setRetMsg(ResponseGCModel.DATA_NOT_FOUND_MSG);
		}
		return responseGCModel;
	}

	/**
	 * wait for all calling apis Futures,then update together(the set of
	 * TypeOfGarbage is duplicate removal)
	 * 
	 * 
	 * @param candidateFutureSet
	 * @param candidateUpdateList
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Async("crisExecutor")
	protected void wait4candidateFuturesAndUpdateDb(Set<Future<List<TypeOfGarbage>>> candidateFutureSet,
			List<TypeOfGarbage> candidateUpdateList) throws InterruptedException, ExecutionException {
		TreeSet<TypeOfGarbage> candidateTogSet = new TreeSet<>((k1, k2) -> {
			int isSame = -1;
			if (k1.getGarbageId() != null && k2.getGarbageId() != null) {
				isSame = k1.getGarbageId().compareTo(k2.getGarbageId());
			} else if (ObjNullUtil.noEmptyOrNullAnd(k1.getGarbageName(), k2.getGarbageName())) {
				isSame = k1.getGarbageName().compareTo(k2.getGarbageName());
			}
			if (isSame == 0) {
				if (k1.getCategoryId() != null && k2.getCategoryId() != null) {
					isSame = k1.getCategoryId().compareTo(k2.getCategoryId());
				} else if (ObjNullUtil.noEmptyOrNullAnd(k1.getCategoryType(), k2.getCategoryType())) {
					isSame = k1.getCategoryType().compareTo(k2.getCategoryType());
				}
			}
			return isSame;
		});
		if (ObjNullUtil.noEmptyOrNull(candidateUpdateList))
			candidateTogSet.addAll(candidateUpdateList);
		long startTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - startTime) <= 30000) {
			if (ObjNullUtil.emptyOrNull(candidateFutureSet)) {
				break;
			}
			for (Future<List<TypeOfGarbage>> future : candidateFutureSet) {
				if (future.isDone()) {
//					System.out.println("one");
//					System.out.println(future);
					candidateFutureSet.remove(future);
					List<TypeOfGarbage> togList = future.get();
					if (ObjNullUtil.noEmptyOrNull(togList))
						candidateTogSet.addAll(togList);
					break;
				}
			}
		}
		// 经过漫长的等待，所有接口调用完毕，也通过TressSet去重完毕，开始进行更新
		updateLocaldbBytog(candidateTogSet);
	}

	public void updateLocaldb(Collection<GarbageClassification> gcList) {
		if (ObjNullUtil.noEmptyOrNull(gcList)) {
			try {
				gcList.forEach(this::updateTask);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void updateLocaldbBytog(Collection<TypeOfGarbage> togList) {
		if (ObjNullUtil.noEmptyOrNull(togList)) {
			togList.forEach((t) -> {
				GarbageClassification exchangedGc = new GarbageClassification();
				Optional<Category> oc = defaultCacheService.getUniqueCategory(t.getCategoryId());
				if (oc.isPresent()) {
					Garbage garbage = new Garbage();
					garbage.setGarbageName(t.getGarbageName());
					garbage.setOrigin(new Short("2"));
					garbage.setOriginAdr(t.getOriginAdr());
					garbage.setGarbageDesc(t.getGarbageDesc());
					exchangedGc.setGarbage(garbage);
					exchangedGc.setCategory(oc.get());
					exchangedGc.setOrigin(new Short("2"));
					exchangedGc.setOriginAdr(t.getOriginAdr());
					try {// 保证批量数据的部分失败而已
						updateTask(exchangedGc);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateTask(GarbageClassification garbageClassification) {
		if (garbageClassification != null && garbageClassification.getCategory() != null
				&& garbageClassification.getGarbage() != null) {
			Category sourceCategory = garbageClassification.getCategory();
			Garbage sourceGarbage = garbageClassification.getGarbage();
			List<Category> candidateCategoryList = null;
			List<Garbage> candidateGarbageList = null;
			// 先检查分类是因为，分类ID必须有保障，该分类必须在我们库里有(主要是分类比较基础，没有功能update，数据少，基本都在缓存里了)
			if (sourceCategory.getCategoryId() == null) {
				if (ObjNullUtil.noEmptyOrNull(sourceCategory.getCategoryType()))
					candidateCategoryList = defaultCacheService.getCategoriesByType(sourceCategory.getCategoryType());
			} else {
				// category是比较基础的数据，可以用缓存查询
				candidateCategoryList = new ArrayList<Category>();
				try {
					candidateCategoryList
							.add(defaultCacheService.getUniqueCategory(sourceCategory.getCategoryId()).get());
				} catch (NoSuchElementException e) {
					LOGGER.error("(╯▔皿▔)╯ updateTask==>entity:" + garbageClassification.toString()
							+ " update to database fail:no such classification");
					e.printStackTrace();
				}
			}
			// 若size=0,即出现新的分类类型，则先不更新，从长计议；若size>1,则数据库分类类型有重名问题
			if (candidateCategoryList != null && candidateCategoryList.size() == 1) {
				GarbageClassification savedGC = null;
				// 目前逻辑是一个垃圾一个分类，所以只查垃圾
				// 由于只需判断有就不更新，所以可以用cache，但如果有gc有删除行为，就建议还是直接查库稳妥
				if (ObjNullUtil.emptyOrNull(ehcacheService.getGCByGarbageIdOrName(sourceGarbage))) {
					if (sourceGarbage.getGarbageId() == null) {
						if (ObjNullUtil.noEmptyOrNull(sourceGarbage.getGarbageName())) {
							candidateGarbageList = ehcacheService.getGarbagesByName(sourceGarbage.getGarbageName());
						} else
							return;
					} else {
						candidateGarbageList = new ArrayList<Garbage>();
						try {// 现在业务没有删除操作，可以用缓存，若垃圾有删除的操作话，建议还是直接查库稳妥
							candidateGarbageList
									.add(ehcacheService.getUniqueGarbage(sourceGarbage.getGarbageId()).get());
						} catch (NoSuchElementException e) {
							LOGGER.error("entity:" + garbageClassification.toString()
									+ " update to database fail:garbageId not exist");
							e.printStackTrace();
							return;
						}
					}
					if (ObjNullUtil.noEmptyOrNull(candidateGarbageList)) {
						// 即使是同名垃圾，加一个就够了
						savedGC = ehcacheService.insertNewGC(candidateGarbageList.get(0), candidateCategoryList.get(0),
								garbageClassification.getOrigin() != null ? garbageClassification.getOrigin()
										: new Short("2"),
								garbageClassification.getOriginAdr());
					} else {
						if (ObjNullUtil.noEmptyOrNull(sourceGarbage.getGarbageName())) {
							Garbage newGarbage = ehcacheService.insertNewGarbage(sourceGarbage.getGarbageName(),
									sourceGarbage.getGarbageDesc(),
									sourceGarbage.getOrigin() != null ? sourceGarbage.getOrigin() : new Short("2"),
									sourceGarbage.getOriginAdr());
							savedGC = ehcacheService.insertNewGC(newGarbage, candidateCategoryList.get(0),
									garbageClassification.getOrigin() != null ? garbageClassification.getOrigin()
											: new Short("2"),
									garbageClassification.getOriginAdr());
						}
					}
					if (savedGC != null && savedGC.getGcId() != null) {
						LOGGER.info("o(*￣▽￣*)o updateTask==>Update new entity of <" + garbageClassification.toString()
								+ "> into local database success.");
					} else {
						LOGGER.error("(╯▔皿▔)╯ updateTask==>Update new entity of <" + garbageClassification.toString()
								+ "> into local database fail.");
					}
				} else {
					LOGGER.warn("(⊙﹏⊙) updateTask==>Please detect if <" + garbageClassification.toString()
							+ "> should be allow to update into local database on the ground that the same garbage was exist in local database.But didnt match this classification.");
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