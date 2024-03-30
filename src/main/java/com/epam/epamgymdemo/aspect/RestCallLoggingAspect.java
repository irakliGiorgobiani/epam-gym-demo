package com.epam.epamgymdemo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RestCallLoggingAspect {

    @Before("execution(* com.epam.epamgymdemo.controller..*.*(..)) ")
    public void logRestCallDetails(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("REST call to {} with arguments {}", methodName, Arrays.toString(args));
    }

    @AfterReturning(pointcut = "execution(* com.epam.epamgymdemo.controller..*.*(..))",
            returning = "result")
    public void logRestCallResponse(Object result) {
        if (result instanceof ResponseEntity<?> responseEntity) {
            log.info("REST call response: status={}, body={}", responseEntity.getStatusCode().value(), responseEntity.getBody());
        }
    }

    @AfterThrowing(pointcut = "execution(* com.epam.epamgymdemo.controller..*.*(..))",
            throwing = "exception")
    public void logRestCallException(Exception exception) {
        log.error("{} in REST call: {}", exception.getClass(), exception.getMessage());
    }
}