package com.rfu.gc.platform.entity;

import java.io.Serializable;

public class TypeOfGarbage implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer garbageId;

	private String garbageName;

	private Integer categoryId;

	private String categoryType;

	private Boolean isFullMatch;

	private String garbageDesc;

	private String categoryExplain;

	private String contain;

	private String tip;

	private String originAdr;

	public Integer getGarbageId() {
		return garbageId;
	}

	public void setGarbageId(Integer garbageId) {
		this.garbageId = garbageId;
	}

	public String getGarbageName() {
		return garbageName;
	}

	public void setGarbageName(String garbageName) {
		this.garbageName = garbageName;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public Boolean getIsFullMatch() {
		return isFullMatch;
	}

	public void setIsFullMatch(Boolean isFullMatch) {
		this.isFullMatch = isFullMatch;
	}

	public String getGarbageDesc() {
		return garbageDesc;
	}

	public void setGarbageDesc(String garbageDesc) {
		this.garbageDesc = garbageDesc;
	}

	public String getCategoryExplain() {
		return categoryExplain;
	}

	public void setCategoryExplain(String categoryExplain) {
		this.categoryExplain = categoryExplain;
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

	public String getOriginAdr() {
		return originAdr;
	}

	public void setOriginAdr(String originAdr) {
		this.originAdr = originAdr;
	}

	@Override
	public String toString() {
		return "TypeOfGarbage [garbageId=" + garbageId + ", garbageName=" + garbageName + ", categoryId=" + categoryId
				+ ", categoryType=" + categoryType + ", isFullMatch=" + isFullMatch + ", garbageDesc=" + garbageDesc
				+ ", categoryExplain=" + categoryExplain + ", contain=" + contain + ", tip=" + tip + ", originAdr="
				+ originAdr + ", toString()=" + super.toString() + "]";
	}
}
