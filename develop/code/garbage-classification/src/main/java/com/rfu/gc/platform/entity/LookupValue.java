package com.rfu.gc.platform.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "t_gc_db_lookup_value")
public class LookupValue implements Serializable{
	private static final long serialVersionUID = 2683438543227845703L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer lkvId;
	
	private String lookupTypeCode;
	
	private String lookupType;
	
	private String lookupKey;
	
	private String lookupValue;
	
	private String valueDesc;
	
	private Integer orderNo;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdTime;
	
	private Short isEnable;

	public Integer getLkvId() {
		return lkvId;
	}

	public void setLkvId(Integer lkvId) {
		this.lkvId = lkvId;
	}

	public String getLookupTypeCode() {
		return lookupTypeCode;
	}

	public void setLookupTypeCode(String lookupTypeCode) {
		this.lookupTypeCode = lookupTypeCode;
	}

	public String getLookupType() {
		return lookupType;
	}

	public void setLookupType(String lookupType) {
		this.lookupType = lookupType;
	}

	public String getLookupKey() {
		return lookupKey;
	}

	public void setLookupKey(String lookupKey) {
		this.lookupKey = lookupKey;
	}

	public String getLookupValue() {
		return lookupValue;
	}

	public void setLookupValue(String lookupValue) {
		this.lookupValue = lookupValue;
	}

	public String getValueDesc() {
		return valueDesc;
	}

	public void setValueDesc(String valueDesc) {
		this.valueDesc = valueDesc;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
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
		return "LookupValue [lkvId=" + lkvId + ", lookupTypeCode=" + lookupTypeCode + ", lookupType=" + lookupType
				+ ", lookupKey=" + lookupKey + ", lookupValue=" + lookupValue + ", valueDesc=" + valueDesc
				+ ", orderNo=" + orderNo + ", createdTime=" + createdTime + ", isEnable=" + isEnable + ", toString()="
				+ super.toString() + "]";
	}
}
