package com.rfu.gc.platform.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Raymond
 *
 */

@Entity
@Table(name = "t_gc_db_garbage_info") // 映射到个表，对应关系
public class Garbage implements Serializable {
	private static final long serialVersionUID = -987249907646098419L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer garbageId;

	private String garbageName;

	private String garbageDesc;

	private Short origin;

	private String originAdr;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdTime;

	private Short isEnable;

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

	public String getGarbageDesc() {
		return garbageDesc;
	}

	public void setGarbageDesc(String garbageDesc) {
		this.garbageDesc = garbageDesc;
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
		return "Garbage [garbageId=" + garbageId + ", garbageName=" + garbageName + ", garbageDesc=" + garbageDesc
				+ ", origin=" + origin + ", originAdr=" + originAdr + ", createdTime=" + createdTime + ", isEnable="
				+ isEnable + ", toString()=" + super.toString() + "]";
	}
}
