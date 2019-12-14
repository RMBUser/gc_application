package com.rfu.gc.platform.entity.choviwu;

import java.util.List;

public class CwGarbageResponseBody {
	private List<CwGarbageInfo> data1;
	private String msg;
	private Integer code;
	public List<CwGarbageInfo> getData1() {
		return data1;
	}
	public void setData1(List<CwGarbageInfo> data1) {
		this.data1 = data1;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
}
