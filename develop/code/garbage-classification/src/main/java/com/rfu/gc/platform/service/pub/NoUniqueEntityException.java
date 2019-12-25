package com.rfu.gc.platform.service.pub;

import java.util.Collection;


public class NoUniqueEntityException extends Exception {
	private static final long serialVersionUID = 5958207894427899882L;
	public NoUniqueEntityException(String message) {
		super(message);
	}
	public NoUniqueEntityException(Collection<?> entityList) {
		super("found for unique "+entityList.toArray()[0].getClass()+" fail:"+entityList.toString()+"has more than one element.");
	}
}
