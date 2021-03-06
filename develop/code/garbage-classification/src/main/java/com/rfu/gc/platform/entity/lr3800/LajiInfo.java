package com.rfu.gc.platform.entity.lr3800;

import com.rfu.gc.platform.pub.extend.FieldMapping;

public class LajiInfo {
	
	@FieldMapping(mappingName = {"garbageName"})
	private String name;
	
	@FieldMapping(mappingName = "")
	private Integer type;
	
	private Integer aipre;
	
	private String explain;
	
	private String contain;
	
	private String tip;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getAipre() {
		return aipre;
	}

	public void setAipre(Integer aipre) {
		this.aipre = aipre;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getContain() {
		return contain;
	}

	public void setContain(String contain) {
		this.contain = contain;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	@Override
	public String toString() {
		return "LajiInfo [name=" + name + ", type=" + type + ", aipre=" + aipre + ", explain=" + explain + ", contain="
				+ contain + ", tip=" + tip + ", toString()=" + super.toString() + "]";
	}
}
