package com.cricket.lane.booking.management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class EventsDto {
    private String id;
    private String eventTitle;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private String location;
    private String description;
    private boolean status;
    private String image;
}
