package com.rfu.gc.platform.entity.choviwu;

import java.util.Date;

public class CwGarbageInfo {
	private Long id;
	private String gname;
	private Date gtime;
	private String gtype;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public Date getGtime() {
		return gtime;
	}
	public void setGtime(Date gtime) {
		this.gtime = gtime;
	}
	public String getGtype() {
		return gtype;
	}
	public void setGtype(String gtype) {
		this.gtype = gtype;
	}
}
