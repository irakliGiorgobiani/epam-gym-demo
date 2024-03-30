package com.epam.epamgymdemo.aspect;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@Slf4j
@Getter
public class TransactionLoggingAspect {

    private final ThreadLocal<String> transactionId = new ThreadLocal<>();

    @Before("execution(* com.epam.epamgymdemo..*.*(..)) && !execution(* com.epam.epamgymdemo.filter..*.*(..))")
    public void generateTransactionId() {
        String newTransactionId = UUID.randomUUID().toString();
        transactionId.set(newTransactionId);
        logTransactionId(newTransactionId);
    }

    private void logTransactionId(String transactionId) {
        log.info("TransactionId: {}", transactionId);
    }

    @After("execution(* com.epam.epamgymdemo..*.*(..)) && !execution(* com.epam.epamgymdemo.filter..*.*(..))")
    public void clearTransactionId() {
        transactionId.remove();
    }
}