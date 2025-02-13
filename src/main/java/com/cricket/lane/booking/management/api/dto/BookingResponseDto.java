package com.cricket.lane.booking.management.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingResponseDto {
    private String bookingId;
    private String userName;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private String phoneNumber;

    public BookingResponseDto(String bookingId, String userName, LocalTime fromTime, LocalTime toTime,String telephoneNumber) {
        this.bookingId = bookingId;
        this.userName = userName;
        this.startTime = fromTime;
        this.endTime = toTime;
        this.phoneNumber = telephoneNumber;
    }

    public BookingResponseDto(String bookingId, LocalTime fromTime, LocalTime toTime) {
        this.bookingId = bookingId;
        this.startTime = fromTime;
        this.endTime = toTime;
    }

}
