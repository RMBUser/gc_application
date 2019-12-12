package com.rfu.gc.platform.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@EnableAspectJAutoProxy
@EnableConfigurationProperties({ExecutorProperties.class})
public class MainConfigurer {
	private final ExecutorProperties executorProperties;
	
	public MainConfigurer(ExecutorProperties executorProperties) {
		this.executorProperties = executorProperties;
	}
	
	@Bean
    public Executor crisExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(executorProperties.getCore().getPoolSize());
        executor.setMaxPoolSize(executorProperties.getCore().getMaxPoolSize());
        executor.setQueueCapacity(executorProperties.getQueue().getCapacity());
        executor.setKeepAliveSeconds(executorProperties.getCore().getKeepAliveSeconds());
        executor.setThreadNamePrefix(executorProperties.getThreadNamePrefix());

        // 线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
