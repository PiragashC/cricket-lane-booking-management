package com.cricket.lane.booking.management.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalenderResponseDto {
    private LocalDate bookingDate;
    private String laneName;
    private String laneId;
    private List<BookingResponseDto> bookingResponseDtos;
    private List<WeekMonthViewResponseDto> weekMonthViewResponseDtos;
}
