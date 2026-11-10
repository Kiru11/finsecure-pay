package com.finsecure.pay.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class TransactionValidationService {

    public ValidationResult validateTransaction(String accountNumber, BigDecimal amount, String currency) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return new ValidationResult(false, "REJECTED", "Invalid or missing account number");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new ValidationResult(false, "REJECTED", "Transaction amount must be greater than zero");
        }
        if (!"USD".equalsIgnoreCase(currency) && !"SGD".equalsIgnoreCase(currency)) {
            return new ValidationResult(false, "REJECTED", "Unsupported currency code");
        }
        if (amount.compareTo(new BigDecimal("10000.00")) > 0) {
            return new ValidationResult(false, "DECLINED", "Amount exceeds maximum transaction limit");
        }

        return new ValidationResult(true, "APPROVED", "Transaction validated successfully");
    }

    public static class ValidationResult {
        private boolean success;
        private String status;
        private String message;

        public ValidationResult(boolean success, String status, String message) {
            this.success = success;
            this.status = status;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getStatus() { return status; }
        public String getMessage() { return message; }
    }
}
