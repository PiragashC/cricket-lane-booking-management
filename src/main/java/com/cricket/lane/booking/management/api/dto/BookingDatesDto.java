package com.cricket.lane.booking.management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class BookingDatesDto {
    private String id;
    private LocalDate bookingDate;
    private String cricketLaneBookingId;
}
