package com.rfu.gc.platform.pub.util;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Async("crisExecutor")
public class AsyncLogbackHelper {
	
	public void trace(Logger logger, String msg) {
		logger.trace(msg);
	}
	public void debug(Logger logger, String msg) {
		logger.debug(msg);
	}
	public void info(Logger logger, String msg) {
		logger.info(msg);
	}
	public void warn(Logger logger, String msg) {
		logger.warn(msg);
	}
	public void error(Logger logger, String msg) {
		logger.error(msg);
	}
}
