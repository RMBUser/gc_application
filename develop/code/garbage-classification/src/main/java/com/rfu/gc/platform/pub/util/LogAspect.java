package com.rfu.gc.platform.pub.util;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
	private Logger logger = LoggerFactory.getLogger("apiLogger");
	@Autowired
	private AsyncLogbackHelper asyncLogbackHelper;

	@Pointcut("execution(public String com.rfu.gc.platform.service.util.*ApiReq.*(String,..))")
	public void apiReqPoint() {
	}

	@AfterThrowing(pointcut = "apiReqPoint()", throwing = "ex")
	public void logIfException(JoinPoint joinPoint, Exception ex) {
		Object[] args = joinPoint.getArgs();
		List<Object> remainArgs = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
		logger.error("(╯▔皿▔)╯  " + joinPoint.getTarget().getClass().getName() + "==>"
				+ joinPoint.getSignature().getName() + "==>Url:" + args[0] + ";Request args:"
				+ remainArgs + ";Calling Api fail.");
		logger.error("	 ┗|｀O′|┛ ==>Reason:", ex);
	}

	@AfterReturning(pointcut = "apiReqPoint()", returning = "obj")
	public void logAfterReturn(JoinPoint joinPoint, Object obj) {
		Object[] args = joinPoint.getArgs();
		List<Object> remainArgs = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
		asyncLogbackHelper.info(logger,
				"o(*￣▽￣*)o " + joinPoint.getTarget().getClass().getName() + "==>" + joinPoint.getSignature().getName()
						+ "==>Url:" + args[0] + ";Request args:" + remainArgs
						+ ";Calling Api success.");
	}
}
