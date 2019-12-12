package com.rfu.gc.platform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cris.executor")
public class ExecutorProperties {
	private final Core core = new Core();
	private final Queue queue = new Queue();
//	@Value("${cris.executor.thread-name-prefix}")
    private String threadNamePrefix;
    
	public Core getCore() {
		return core;
	}
	public Queue getQueue() {
		return queue;
	}
	public String getThreadNamePrefix() {
		return threadNamePrefix;
	}
	public void setThreadNamePrefix(String threadNamePrefix) {
		this.threadNamePrefix = threadNamePrefix;
	}
	public class Core {
//	@Value("${cris.executor.core.pool-size}")
		private int poolSize;
//	@Value("${cris.executor.core.max-pool-size}")
		private int maxPoolSize;
//	@Value("${cris.executor.core.keep-alive-seconds}")
		private int keepAliveSeconds;
		
		public int getPoolSize() {
			return poolSize;
		}
		public void setPoolSize(int poolSize) {
			this.poolSize =poolSize;
		}
		public int getMaxPoolSize() {
			return maxPoolSize;
		}
		public void setMaxPoolSize(int maxPoolSize) {
			this.maxPoolSize = maxPoolSize;
		}
		public int getKeepAliveSeconds() {
			return keepAliveSeconds;
		}
		public void setKeepAliveSeconds(int keepAliveSeconds) {
			this.keepAliveSeconds = keepAliveSeconds;
		}
	}
	public class Queue {
//	@Value("${cris.executor.queue.capacity}")
		private int capacity;
		
		public int getCapacity() {
			return capacity;
		}
		public void setCapacity(int capacity) {
			this.capacity = capacity;
		}
	}
}
