package com.cricket.lane.booking.management.service;

import com.cricket.lane.booking.management.api.dto.PaymentResponseDto;
import com.cricket.lane.booking.management.entity.CricketLaneBooking;
import com.cricket.lane.booking.management.exception.ServiceException;
import com.cricket.lane.booking.management.repository.CricketLaneBookingRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

import static com.cricket.lane.booking.management.constants.ApplicationConstants.BAD_REQUEST;
import static com.cricket.lane.booking.management.constants.ApplicationConstants.BOOKING_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CricketLaneBookingRepository cricketLaneBookingRepository;

    @Value("${stripe.public.key}")
    private String publicKey;

    public ResponseEntity<?> createPaymentIntent(String bookingId) {
        try {
            CricketLaneBooking cricketLaneBooking = cricketLaneBookingRepository.findById(bookingId)
                    .orElseThrow(() -> new ServiceException(BOOKING_ID_NOT_FOUND, BAD_REQUEST, HttpStatus.BAD_REQUEST));

            // Assuming the tax rate for HST (Canada 13%) or get from Stripe Tax API
            BigDecimal taxRate = BigDecimal.valueOf(0.13); // Example: 13% HST for Canada
            BigDecimal bookingPrice = cricketLaneBooking.getBookingPrice();

            // Calculate tax (e.g., 13% HST)
            BigDecimal taxAmount = bookingPrice.multiply(taxRate);

            // Total amount with tax
            BigDecimal totalAmountWithTax = bookingPrice.add(taxAmount);

            // Convert total amount to cents (Stripe expects amounts in the smallest currency unit, e.g., cents)
            long amountInCents = totalAmountWithTax.multiply(BigDecimal.valueOf(100)).longValue();

            // Create PaymentIntent with tax
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setCurrency("cad")
                    .setAmount(amountInCents)  // Total amount including tax
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            return ResponseEntity.ok().body(
                    new PaymentResponseDto(paymentIntent.getClientSecret())
            );

        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    public PaymentResponseDto getStripePublishableKey() {
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setSTRIPE_PUBLISHABLE_KEY(publicKey);
        return paymentResponseDto;
    }
}
