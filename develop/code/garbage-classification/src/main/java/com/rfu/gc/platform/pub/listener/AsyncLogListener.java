package com.rfu.gc.platform.pub.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AsyncLogListener implements ApplicationListener<LogbackEvent> {

	private Logger logger = LoggerFactory.getLogger("commentLogger");
	@Override
	public void onApplicationEvent(LogbackEvent event) {
		String msg = event.getSource();
		if(event.getLevel()==null)return;
		String level = event.getLevel().trim().toLowerCase();
		switch (level) {
		case LogbackEvent.TRACE:
			logger.trace(msg);
			break;
		case LogbackEvent.DEBUG:
			logger.debug(msg);
			break;
		case LogbackEvent.INFO:
			logger.info(msg);
			break;
		case LogbackEvent.WARN:
			logger.warn(msg);
			break;
		case LogbackEvent.ERROR:
			logger.error(msg);
			break;
		default:
			logger.info(msg);
			break;
		}
	}

}
