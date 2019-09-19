package com.rfu.gc.platform.pub.listener;

import org.springframework.context.ApplicationEvent;

public class LogbackEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	private String level;
	
	public static final String TRACE = "trace";
	
	public static final String DEBUG = "debug";
	
	public static final String INFO = "info";
	
	public static final String WARN = "warn";
	
	public static final String ERROR = "error";

	public LogbackEvent(String msg, String level) {
		super(msg);
		this.level = level;
	}
	
	@Override
	public String getSource() {
		return (String)super.getSource();
	}
	
	public String getLevel() {
		return level;
	}
}
