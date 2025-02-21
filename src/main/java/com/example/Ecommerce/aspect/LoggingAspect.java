package com.example.Ecommerce.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.example.Ecommerce.service.impl.*.*(..))")
    public void beforeServiceMethodInvoking(JoinPoint joinPoint){
       LOGGER.info(joinPoint.getSignature().getName()+"(): Service method invoked");
    }

    @Before("execution(* com.example.Ecommerce.controller.*.*(..))")
    public void beforeControllerMethodInvoking(JoinPoint joinPoint){
        LOGGER.info(joinPoint.getSignature().getName()+"(): Controller method invoked");
    }

    @After("execution(* com.example.Ecommerce.service.impl.*.*(..))")
    public void afterServiceMethodInvoking(JoinPoint joinPoint){
        LOGGER.info(joinPoint.getSignature().getName()+"(): Service method executed successfully");
    }

    @After("execution(* com.example.Ecommerce.controller.*.*(..))")
    public void afterControllerMethodInvoking(JoinPoint joinPoint){
        LOGGER.info(joinPoint.getSignature().getName()+"(): Controller method executed successfully");
    }
}
