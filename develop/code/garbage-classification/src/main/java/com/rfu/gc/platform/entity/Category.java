package com.rfu.gc.platform.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "t_gc_db_category")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer categoryId;
	
	private String categoryType;
	
	private Short origin;
	
	private String originAdr;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdTime;
	
	private Short isEnable;
	
	private String categoryExplain;
	
	private String contain;
	
	private String tip;

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

	public Short getOrigin() {
		return origin;
	}

	public void setOrigin(Short origin) {
		this.origin = origin;
	}

	public String getOriginAdr() {
		return originAdr;
	}

	public void setOriginAdr(String originAdr) {
		this.originAdr = originAdr;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Short getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Short isEnable) {
		this.isEnable = isEnable;
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

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", categoryType=" + categoryType + ", origin=" + origin
				+ ", originAdr=" + originAdr + ", createdTime=" + createdTime + ", isEnable=" + isEnable + ", desc="
				+ ", categoryExplain=" + categoryExplain + ", contain=" + contain + ", tip=" + tip
				+ ", toString()=" + super.toString() + "]";
	}
}
