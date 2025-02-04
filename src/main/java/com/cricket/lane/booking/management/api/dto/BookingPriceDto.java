package com.cricket.lane.booking.management.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookingPriceDto {
    private BigDecimal bookingPrice;
}
