package com.cricket.lane.booking.management.agent;

import com.cricket.lane.booking.management.agent.converter.LaneConverter;
import com.cricket.lane.booking.management.api.dto.LaneDto;
import com.cricket.lane.booking.management.api.dto.ResponseDto;
import com.cricket.lane.booking.management.service.LaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LaneAgent {

    private final LaneService laneService;

    private final LaneConverter laneConverter;

    public List<LaneDto> getAllActiveLanes() {
        return laneService.getAllActiveLanes();
    }

    public ResponseDto createLane(LaneDto laneDto) {
        return laneService.createLane(laneConverter.convert(laneDto));
    }

    public ResponseDto updateLaneStatus(String id, boolean status) {
        return laneService.updateLaneStatus(id,status);
    }
}
