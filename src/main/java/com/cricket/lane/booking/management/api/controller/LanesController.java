package com.cricket.lane.booking.management.api.controller;

import com.cricket.lane.booking.management.agent.LaneAgent;
import com.cricket.lane.booking.management.api.dto.LaneDto;
import com.cricket.lane.booking.management.api.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/lanes")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://sports-booking-hub.onrender.com")
public class LanesController {

    private final LaneAgent laneAgent;

    @PostMapping
    public ResponseDto createLane(@RequestBody LaneDto laneDto){
        return laneAgent.createLane(laneDto);
    }

    @GetMapping
    public List<LaneDto> getAllLanes(){
        return laneAgent.getAllActiveLanes();
    }
}
