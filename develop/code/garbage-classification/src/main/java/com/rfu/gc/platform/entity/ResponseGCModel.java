package com.rfu.gc.platform.entity;

import java.io.Serializable;

public class ResponseGCModel<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Integer SUCCESS = 200, DATA_NOT_FOUND_CODE = 300, API_CALL_FAIL_CODE = 600,
			UNKNOW_ERROR_CODE = 900;

	public static final String SUCCESS_MSG = "success", DATA_NOT_FOUND_MSG = "data is not found form system",
			API_CALL_FAIL_MSG = "call api fail", UNKNOW_ERROR_MSG = "unknow error";

	private Integer retCode;

	private String retMsg;

	private T data;

	public ResponseGCModel() {
		retCode = UNKNOW_ERROR_CODE;
		retMsg = UNKNOW_ERROR_MSG;
	}

	public Integer getRetCode() {
		return retCode;
	}

	public void setRetCode(Integer retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
