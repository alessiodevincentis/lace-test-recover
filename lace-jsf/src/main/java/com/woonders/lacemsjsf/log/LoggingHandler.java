package com.woonders.lacemsjsf.log;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by epapa on 19/10/2016.
 */
@Aspect
@Component
@Slf4j
public class LoggingHandler {

	@Pointcut("within(@org.springframework.stereotype.Service *)")
	public void beanAnnotatedWithService() {
	}

	@Pointcut("execution(public * *(..))")
	public void publicMethod() {
	}

	// before -> Any resource annotated with @Service annotation - log method
	// name and parameters
	@Before("beanAnnotatedWithService() && publicMethod()")
	public void logBefore(final JoinPoint joinPoint) {
		//loggando ogni tenant in un file diverso questo non serve piu...
		//log.info("Tenant : " + httpServletRequest.getHeader("host"));
		//pero possiamo loggarci l-username, why not
		String username = "not logged in yet!";
		if(SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
			username = SecurityContextHolder.getContext().getAuthentication().getName();
		}
		log.info("Username : " + username);
		log.info("Entering in Method :  " + joinPoint.getSignature().getName());
		log.info("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName());
		log.info("Arguments :  " + Arrays.toString(joinPoint.getArgs()));
		log.info("Target class : " + joinPoint.getTarget().getClass().getName());
	}

	// After -> Any resource annotated with @Service annotation - log return
	// value
	@AfterReturning(pointcut = "beanAnnotatedWithService() && publicMethod()", returning = "result")
	public void logAfter(final JoinPoint joinPoint, final Object result) {
		if (result != null) {
			log.info("Method Return value : " + result.toString());
		}
	}

	// Around -> Any resource annotated with @Service annotation - log elapsed
	// time
	@Around("beanAnnotatedWithService() && publicMethod()")
	public Object logAround(final ProceedingJoinPoint joinPoint) throws Throwable {

		final long start = System.currentTimeMillis();
		try {
			final String className = joinPoint.getSignature().getDeclaringTypeName();
			final String methodName = joinPoint.getSignature().getName();
			final Object result = joinPoint.proceed();
			final long elapsedTime = System.currentTimeMillis() - start;
			log.debug("Method " + className + "." + methodName + " ()" + " execution time : " + elapsedTime + " ms");
			return result;
		} catch (final IllegalArgumentException e) {
			log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
					+ joinPoint.getSignature().getName() + "()");
			throw e;
		}
	}

	// After -> Any resource annotated with @Service annotation - log exception
	@AfterThrowing(pointcut = "beanAnnotatedWithService() && publicMethod()", throwing = "exception")
	public void logAfterThrowing(final JoinPoint joinPoint, final Throwable exception) {
		log.error("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
		log.error("Exception : " + exception);
		if (exception.getStackTrace() != null && exception.getStackTrace().length > 0
				&& exception.getStackTrace()[0] != null) {
			log.error("Exception error: " + exception.getStackTrace()[0]);
		}
	}
	
}
