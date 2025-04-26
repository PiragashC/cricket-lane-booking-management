package com.cricket.lane.booking.management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class LaneCardDto {
    private String id;
    private String laneCardTitle;
    private String frequency;
    private String timeInterval;
    private String ratePerHour;
}
