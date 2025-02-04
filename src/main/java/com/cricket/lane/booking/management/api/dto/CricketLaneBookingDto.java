package com.cricket.lane.booking.management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class CricketLaneBookingDto {
    private String id;
    private String email;
    private LocalTime fromTime;
    private LocalTime toTime;
    private Set<SelectedLanesDto> selectedLanesDtos;
    private Set<BookingDatesDto> bookingDatesDtos;
    private String bookingTitle;
    private String bookingDetails;
    private String firstName;
    private String lastName;
    private String telephoneNumber;
    private String organization;
    private BigDecimal bookingPrice;
    private String bookingStatus;
}
