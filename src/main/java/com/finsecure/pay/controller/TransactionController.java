package com.finsecure.pay.controller;

import com.finsecure.pay.model.TransactionRequest;
import com.finsecure.pay.model.TransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TransactionController {

    @GetMapping("/health")
    public ResponseEntity healthCheck() {
        Map<String, String> body = new HashMap<>();
        body.put("status", "UP");
        body.put("service", "FinSecure Pay Validation API");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/validate")
    public ResponseEntity<TransactionResponse> validateTransaction(@RequestBody TransactionRequest request) {
        if (request.getAmount() <= 0) {
            return ResponseEntity.badRequest()
                    .body(new TransactionResponse("FAILED", "Validation Error: Amount must be greater than zero."));
        }

        if (!"USD".equalsIgnoreCase(request.getCurrency()) && !"SGD".equalsIgnoreCase(request.getCurrency())) {
            return ResponseEntity.badRequest()
                    .body(new TransactionResponse("FAILED", "Validation Error: Unsupported currency. Only USD and SGD are allowed."));
        }

        if (request.getAccountNumber() == null || !request.getAccountNumber().startsWith("ACC")) {
            return ResponseEntity.badRequest()
                    .body(new TransactionResponse("FAILED", "Validation Error: Invalid account number format."));
        }

        return ResponseEntity.ok(new TransactionResponse("SUCCESS", "Transaction passed all validation checks."));
    }
}
