package com.example.junsta.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;



@Component
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE)
public class AspectLogger {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	

	@Around("execution(* com.example.junsta.*.*Controller.*(..))")
	public Object loggingResponseAndRequest(final ProceedingJoinPoint joinPoint) throws Throwable {
		
		Object[] params = joinPoint.getArgs();
		String paramStr = Arrays.toString(params);
		logger.info("======================================================================================================================================");
		logger.info("| ClassName:			>>>  {}  ", joinPoint.getTarget().getClass().getName() );
		logger.info("| SignatureName:		>>>  {}  ", joinPoint.getSignature().getName() );
		logger.info("| requestParameters:	>>>  {}  ", paramStr );
		logger.info("======================================================================================================================================");
		
		//이걸 기점으로 메소드 실행후 실행
		Object result = joinPoint.proceed();
		
		String responseStr = result.toString();
		logger.info("======================================================================================================================================");
		logger.info("| response: >>>  {}  ", responseStr );
	    logger.info("======================================================================================================================================");
	    
	    return result;
	}
}
