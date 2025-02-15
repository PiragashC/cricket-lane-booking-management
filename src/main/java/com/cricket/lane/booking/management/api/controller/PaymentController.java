package com.cricket.lane.booking.management.api.controller;

import com.cricket.lane.booking.management.api.dto.PaymentResponseDto;
import com.cricket.lane.booking.management.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://sports-booking-hub.onrender.com"})
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestParam String bookingId){
        return paymentService.createPaymentIntent(bookingId);
    }

    @GetMapping("/config")
    public PaymentResponseDto getStripePublishableKey(){
        return paymentService.getStripePublishableKey();
    }
}
