package com.cricket.lane.booking.management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class SelectedLanesDto {
    private String id;
    private String laneId;
    private String cricketLaneBookingId;
}
