package com.cricket.lane.booking.management.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class PaymentResponseDto {
    private String clientSecret;
    private String STRIPE_PUBLISHABLE_KEY;

    public PaymentResponseDto(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
