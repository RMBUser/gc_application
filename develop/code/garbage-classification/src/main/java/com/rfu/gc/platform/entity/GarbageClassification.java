package com.rfu.gc.platform.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "t_gc_bu_garbage_classification")
public class GarbageClassification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gcId;

	@JoinColumn(name = "GARBAGE_ID", referencedColumnName = "garbageId")
	@ManyToOne
	private Garbage garbage;

	@JoinColumn(name = "CATEGORY_ID", referencedColumnName = "categoryId")
	@ManyToOne
	private Category category;

	private Short origin;

	private String originAdr;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdTime;

	private Short isEnable;

	public Integer getGcId() {
		return gcId;
	}

	public void setGcId(Integer gcId) {
		this.gcId = gcId;
	}

	public Garbage getGarbage() {
		return garbage;
	}

	public void setGarbage(Garbage garbage) {
		this.garbage = garbage;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	@Override
	public String toString() {
		return "GarbageClassification [gcId=" + gcId + ", garbage=" + garbage + ", category=" + category + ", origin="
				+ origin + ", originAdr=" + originAdr + ", createdTime=" + createdTime + ", isEnable=" + isEnable
				+ ", toString()=" + super.toString() + "]";
	}
}
