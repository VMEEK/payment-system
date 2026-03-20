package org.cherub.paymentEndpoint.controllers;


import org.cherub.paymentEndpoint.dtos.FeePaymentDto;
import org.cherub.paymentEndpoint.dtos.FeePaymentResponseDto;
import org.cherub.paymentEndpoint.models.FeePayment;
import org.cherub.paymentEndpoint.services.FeePaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Tag(name = "Payments", description = "Endpoints for processing student payments")
public class FeePaymentController {

    private final FeePaymentService feePaymentService;

    public FeePaymentController(FeePaymentService feePaymentService) {
        this.feePaymentService = feePaymentService;
    }

    private static FeePaymentResponseDto getFeePaymentResponseDto(FeePayment feePayment) {

        FeePaymentResponseDto response = new FeePaymentResponseDto();
        response.setStudentNumber(feePayment.getStudentNumber());
        response.setPaymentAmount(feePayment.getPaymentAmount());
        response.setNextPaymentDueDate(feePayment.getNextPaymentDueDate());
        response.setIncentiveRate(feePayment.getIncentiveRate());
        response.setIncentiveAmount(feePayment.getIncentiveAmount());
        response.setPreviousBalance(feePayment.getPreviousBalance());
        response.setNewBalance(feePayment.getNewBalance());
        return response;
    }


    @PostMapping("/one-time-payment")
    public ResponseEntity<?> makePayments(@RequestBody FeePaymentDto feePaymentDto) {
        try {
            FeePayment feePayment = feePaymentService.processPayment(feePaymentDto);
            FeePaymentResponseDto response = getFeePaymentResponseDto(feePayment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}

