package com.epam.epamgymdemo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@Slf4j
class RestCallLoggingAspectTest {

    @Mock
    private JoinPoint joinPoint;

    @Test
    void testLogRestCallDetails() {
        when(joinPoint.getSignature()).thenReturn(mock(MethodSignature.class));
        when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1", "arg2"});

        RestCallLoggingAspect aspect = new RestCallLoggingAspect();
        aspect.logRestCallDetails(joinPoint);

        verify(joinPoint).getSignature();
        verify(joinPoint).getArgs();
    }

    @Test
    void testLogRestCallResponse() {
        ResponseEntity<String> responseEntity = ResponseEntity.ok("test response");

        RestCallLoggingAspect aspect = new RestCallLoggingAspect();
        aspect.logRestCallResponse(responseEntity);

        log.info("REST call response: status={}, body={}",
                responseEntity.getStatusCode().value(), responseEntity.getBody());
    }

    @Test
    void testLogRestCallException() {
        Exception exception = new RuntimeException("test exception");

        RestCallLoggingAspect aspect = new RestCallLoggingAspect();
        aspect.logRestCallException(exception);

        log.error("{} in REST call: {}", exception.getClass(), exception.getMessage());
    }
}

