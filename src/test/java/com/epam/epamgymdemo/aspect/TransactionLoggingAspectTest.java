package com.epam.epamgymdemo.aspect;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransactionLoggingAspectTest {

    @Test
    void testGenerateTransactionId() {
        TransactionLoggingAspect aspect = new TransactionLoggingAspect();

        aspect.generateTransactionId();

        assertNotNull(aspect.getTransactionId());
    }

    @Test
    void testClearTransactionId() {
        TransactionLoggingAspect aspect = new TransactionLoggingAspect();

        aspect.clearTransactionId();

        assertNull(aspect.getTransactionId().get());
    }
}
