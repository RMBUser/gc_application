package com.rfu.gc.platform.entity;

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
@Table(name = "t_gc_db_garbage_info") //映射到个表，对应关系
public class Garbage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer garbageId;
	
	private String garbageName;
	
	private String desc;
	
	private String origin;
	
	private String originAdr;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdTime;
	
	private Integer isEnable;

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

	public String getDesc() {
		return desc;
	}

	public void setDes(String desc) {
		this.desc = desc;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
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

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}
	
	
	

}
