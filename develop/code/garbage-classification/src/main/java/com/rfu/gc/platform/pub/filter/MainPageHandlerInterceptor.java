package com.rfu.gc.platform.pub.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class MainPageHandlerInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(new Integer(404).equals(response.getStatus())) {
			request.getRequestDispatcher("/").forward(request, response);
			return false;
		}
		return true;
	}
}
