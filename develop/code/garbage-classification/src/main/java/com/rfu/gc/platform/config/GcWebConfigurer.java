package com.rfu.gc.platform.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.rfu.gc.platform.pub.filter.MainPageHandlerInterceptor;

@Configuration
public class GcWebConfigurer implements WebMvcConfigurer {
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
	}
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/classify/**").allowedMethods("GET","POST");
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> excludePaths = Arrays.asList("/","/classify/**");
		registry.addInterceptor(new MainPageHandlerInterceptor()).excludePathPatterns(excludePaths);
	}
}
