package com.finsecure.pay.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void testSuccessfulValidation() throws Exception {
        String payload = "{\"accountNumber\":\"ACC98765\",\"amount\":250.50,\"currency\":\"USD\"}";
        mockMvc.perform(post("/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void testFailedValidationInvalidAmount() throws Exception {
        String payload = "{\"accountNumber\":\"ACC98765\",\"amount\":-50.00,\"currency\":\"USD\"}";
        mockMvc.perform(post("/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("FAILED"));
    }

    @Test
    void testFailedValidationUnsupportedCurrency() throws Exception {
        String payload = "{\"accountNumber\":\"ACC98765\",\"amount\":100.00,\"currency\":\"EUR\"}";
        mockMvc.perform(post("/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("FAILED"));
    }

//    @Test
//    void intentionalFailureTest() {
        // Forcing a failure to demonstrate pipeline gatekeeping
//        org.junit.jupiter.api.Assertions.assertEquals(true, false, "Intentional test failure for CI/CD demonstration");
//    } 
}
