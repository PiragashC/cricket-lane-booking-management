package com.cricket.lane.booking.management.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LaneDto {
    private String laneId;
    private String laneName;

    public LaneDto(String id, String laneName) {
        this.laneId = id;
        this.laneName = laneName;
    }
}
