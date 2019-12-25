package com.rfu.gc.platform.entity.choviwu;

import java.util.List;

public class CwGarbageResponseBody {
	private List<CwGarbageInfo> data;
	private String msg;
	private Integer code;
	public List<CwGarbageInfo> getData() {
		return data;
	}
	public void setData(List<CwGarbageInfo> data) {
		this.data = data;
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
	@Override
	public String toString() {
		return "CwGarbageResponseBody [data=" + data + ", msg=" + msg + ", code=" + code + ", toString()="
				+ super.toString() + "]";
	}
}
