package com.rfu.gc.platform.config;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ResourceCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import net.sf.ehcache.CacheManager;

@Configuration
@EnableAsync(mode = AdviceMode.ASPECTJ)
@EnableAspectJAutoProxy
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class MainConfigurer {

	@Bean
	@ConditionalOnMissingBean
	public CacheManagerCustomizers cacheManagerCustomizers(ObjectProvider<CacheManagerCustomizer<?>> customizers) {
		return new CacheManagerCustomizers(customizers.orderedStream().collect(Collectors.toList()));
	}

	@Configuration
	@EnableConfigurationProperties({ ExecutorProperties.class })
	static class ThreadPoolConfig {
		private final ExecutorProperties executorProperties;

		public ThreadPoolConfig(ExecutorProperties executorProperties) {
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

			// 线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，当线程池没有处理能力的时候，该策略会直接在 execute
			// 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
			executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			executor.initialize();
			return executor;
		}
	}

	@Configuration
	@EnableCaching(mode = AdviceMode.ASPECTJ)
	@EnableConfigurationProperties(CacheProperties.class)
	static class CacheConfig extends CachingConfigurerSupport implements ApplicationContextAware {

		private ApplicationContext applicationContext;

		private final CacheProperties cacheProperties;

		private final CacheManagerCustomizers customizers;

		public CacheConfig(CacheProperties cacheProperties, CacheManagerCustomizers customizers) {
			this.cacheProperties = cacheProperties;
			this.customizers = customizers;
		}

		@Override
		public org.springframework.cache.CacheManager cacheManager() {
			return (org.springframework.cache.CacheManager) applicationContext.getBean("defaultCacheManager");
		}

		@Bean("defaultCacheManager")
		public ConcurrentMapCacheManager defaultCacheManager() {
			ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
			List<String> cacheNames = this.cacheProperties.getCacheNames();
			if (!cacheNames.isEmpty()) {
				cacheManager.setCacheNames(cacheNames);
			}
			return this.customizers.customize(cacheManager);
		}

		@Bean("ehcacheManager")
		@Conditional({ EhcacheCondition.class })
		public EhCacheCacheManager ehcacheManager(CacheManager ehCacheCacheManager) {
			return this.customizers.customize(new EhCacheCacheManager(ehCacheCacheManager));
		}

		@Bean
		@Conditional({ EhcacheCondition.class })
		public CacheManager ehCacheCacheManager() {
			Resource location = this.cacheProperties
					.resolveConfigLocation(this.cacheProperties.getEhcache().getConfig());
			if (location != null) {
				return EhCacheManagerUtils.buildCacheManager(location);
			}
			return EhCacheManagerUtils.buildCacheManager();
		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
		}

	}

	/**
	 * Determine if the EhCache configuration is available. This either kick in if a
	 * default configuration has been found or if property referring to the file to
	 * use has been set.
	 */
	static class EhcacheCondition extends ResourceCondition {

		EhcacheCondition() {
			super("EhCache", "spring.cache.ehcache.config", "classpath:/ehcache.xml");
		}
	}
}
