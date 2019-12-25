package com.rfu.gc.platform.entity.choviwu;

import com.rfu.gc.platform.pub.extend.FieldMapping;

public class CwGarbageInfo {
	private Long id;
	@FieldMapping(mappingName = { "garbageName" })
	private String gname;
	@FieldMapping(mappingName = { "categoryType" })
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

	public String getGtype() {
		return gtype;
	}

	public void setGtype(String gtype) {
		this.gtype = gtype;
	}

	@Override
	public String toString() {
		return "CwGarbageInfo [id=" + id + ", gname=" + gname + ", gtype=" + gtype + ", toString()=" + super.toString()
				+ "]";
	}
}
