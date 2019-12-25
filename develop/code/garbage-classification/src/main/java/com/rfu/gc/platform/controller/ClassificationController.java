package com.rfu.gc.platform.controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
		ResponseGCModel<List<TypeOfGarbage>> responseModel = null;
		List<TypeOfGarbage> togList = localGCService.queryGCByStr(garbageName);
		if (ObjNullUtil.noEmptyOrNull(togList)) {
			responseModel = new ResponseGCModel<>();
			responseModel.setRetCode(ResponseGCModel.SUCCESS);
			responseModel.setRetMsg(ResponseGCModel.SUCCESS_MSG);
			responseModel.setData(togList);
			return responseModel;
		}
		try {
			if ((responseModel = future.get(5, TimeUnit.SECONDS))!=null) return responseModel;
		} catch (InterruptedException | ExecutionException e) {
			responseModel = new ResponseGCModel<>();
			responseModel.setRetCode(ResponseGCModel.UNKNOW_ERROR_CODE);
			responseModel.setRetMsg(ResponseGCModel.UNKNOW_ERROR_MSG);
			e.printStackTrace();
		} catch (TimeoutException e) {
			responseModel = new ResponseGCModel<>();
			responseModel.setRetCode(ResponseGCModel.RESPONSE_TIME_OUT);
			responseModel.setRetMsg(ResponseGCModel.RESPONSE_TIME_OUT_MSG);
			e.printStackTrace();
		}
		responseModel.setData(Arrays.asList(new TypeOfGarbage[0]));
		return responseModel;
	}
}
