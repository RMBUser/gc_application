package com.rfu.gc.platform.entity.lr3800;

import java.util.List;

public class LajiResponseBody {
	
	private Integer code;
	
	private String msg;
	
	private List<LajiInfo> newslist;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<LajiInfo> getNewslist() {
		return newslist;
	}

	public void setNewslist(List<LajiInfo> newslist) {
		this.newslist = newslist;
	}
	
}
