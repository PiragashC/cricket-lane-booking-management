package com.cricket.lane.booking.management.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PromoCodeDto {
    private String id;
    private String promoCode;
    private BigDecimal discount;
}
