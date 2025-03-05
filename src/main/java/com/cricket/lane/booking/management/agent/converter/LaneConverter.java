package com.cricket.lane.booking.management.agent.converter;

import com.cricket.lane.booking.management.api.dto.LaneDto;
import com.cricket.lane.booking.management.entity.Lanes;
import org.springframework.stereotype.Component;

@Component
public class LaneConverter {

    public Lanes convert(LaneDto laneDto){
        return Lanes.builder()
                .id(laneDto.getLaneId())
                .laneName(laneDto.getLaneName())
                .isActive(Boolean.TRUE)
                .lanePrice(laneDto.getLanePrice())
                .build();
    }

    public Lanes convert(Lanes existingLane, LaneDto laneDto){
        existingLane.setId(laneDto.getLaneId());
        existingLane.setLaneName(laneDto.getLaneName());
        existingLane.setLanePrice(laneDto.getLanePrice());
        return existingLane;
    }
}
