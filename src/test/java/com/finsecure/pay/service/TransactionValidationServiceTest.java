package com.finsecure.pay.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionValidationServiceTest {

    private TransactionValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new TransactionValidationService();
    }

    @Test
    void testValidTransactionApproved() {
        TransactionValidationService.ValidationResult result = 
            validationService.validateTransaction("ACC12345", new BigDecimal("500.00"), "USD");

        assertTrue(result.isSuccess());
        assertEquals("APPROVED", result.getStatus());
    }

    @Test
    void testInvalidAccountNumberRejected() {
        TransactionValidationService.ValidationResult result = 
            validationService.validateTransaction("", new BigDecimal("100.00"), "USD");

        assertFalse(result.isSuccess());
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testInvalidAmountRejected() {
        TransactionValidationService.ValidationResult result = 
            validationService.validateTransaction("ACC12345", new BigDecimal("0.00"), "USD");

        assertFalse(result.isSuccess());
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testExceedsLimitDeclined() {
        TransactionValidationService.ValidationResult result = 
            validationService.validateTransaction("ACC12345", new BigDecimal("15000.00"), "SGD");

        assertFalse(result.isSuccess());
        assertEquals("DECLINED", result.getStatus());
    }
}
