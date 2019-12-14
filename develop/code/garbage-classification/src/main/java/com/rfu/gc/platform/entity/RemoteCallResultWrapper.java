package com.rfu.gc.platform.entity;

import java.util.List;

public class RemoteCallResultWrapper <T> {
	
	private T target;
	
	private List<GarbageClassification> garbageClassificationList;

	public T getTarget() {
		return target;
	}

	public void setTarget(T target) {
		this.target = target;
	}

	public List<GarbageClassification> getGarbageClassificationList() {
		return garbageClassificationList;
	}

	public void setGarbageClassificationList(List<GarbageClassification> garbageClassificationList) {
		this.garbageClassificationList = garbageClassificationList;
	}
}
