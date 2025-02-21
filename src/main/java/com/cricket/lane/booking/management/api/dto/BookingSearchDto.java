package com.cricket.lane.booking.management.api.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Data
@Builder
public class BookingSearchDto {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String laneId;
    private String status;
    private String type;
    private int page;
    private int size;
}
