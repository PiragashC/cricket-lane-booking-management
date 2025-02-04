package com.cricket.lane.booking.management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class AvailabilityResponseDto {
    private LocalDate date;
    private List<LaneDto> laneDtos;
}
