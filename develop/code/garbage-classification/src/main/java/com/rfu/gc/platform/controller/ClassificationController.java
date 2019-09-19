package com.rfu.gc.platform.controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rfu.gc.platform.entity.ResponseGCModel;
import com.rfu.gc.platform.entity.TypeOfGarbage;
import com.rfu.gc.platform.pub.util.ObjNullUtil;
import com.rfu.gc.platform.service.LocalGCService;
import com.rfu.gc.platform.service.RemoteGCServive;

@RestController
@RequestMapping(value = "/classify")
public class ClassificationController {

	@Autowired
	private LocalGCService localGCService;
	@Autowired
	private RemoteGCServive remoteGCServive;

	@GetMapping(value = "/byStr")
	public ResponseGCModel<List<TypeOfGarbage>> classifyByStr(@RequestParam(required = true) String garbageName) {
		Future<ResponseGCModel<List<TypeOfGarbage>>> future = remoteGCServive.ask4ResultFromLr3800(garbageName);
		List<TypeOfGarbage> togList = localGCService.queryGCByStr(garbageName);
		if (ObjNullUtil.noEmptyOrNull(togList)) {
			ResponseGCModel<List<TypeOfGarbage>> localResponseGCModel = new ResponseGCModel<>();
			localResponseGCModel.setRetCode(ResponseGCModel.SUCCESS);
			localResponseGCModel.setRetMsg(ResponseGCModel.SUCCESS_MSG);
			localResponseGCModel.setData(togList);
			return localResponseGCModel;
		}
		//避免请求接口时间过长（网络问题、处理时间长等问题），只为了减少循环带来的开销，分开循环和判断，让future还未完之前先阻塞一下，以减少循环数
		while (!future.isDone()) {
			synchronized (garbageName) {
				garbageName.notify();
			}
		}
		if (future.isDone()) {
			try {
				return future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		ResponseGCModel<List<TypeOfGarbage>> emptyResponseGCModel = new ResponseGCModel<>();
		emptyResponseGCModel.setRetCode(ResponseGCModel.UNKNOW_ERROR_CODE);
		emptyResponseGCModel.setRetMsg(ResponseGCModel.UNKNOW_ERROR_MSG);
		emptyResponseGCModel.setData(Arrays.asList(new TypeOfGarbage[0]));
		return emptyResponseGCModel;
	}
}
