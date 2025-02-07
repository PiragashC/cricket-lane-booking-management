package com.cricket.lane.booking.management.api.controller;

import com.cricket.lane.booking.management.agent.LaneAgent;
import com.cricket.lane.booking.management.api.dto.LaneDto;
import com.cricket.lane.booking.management.api.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/lane")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class LaneController {

    private final LaneAgent laneAgent;

    @GetMapping
    public List<LaneDto> getAllActiveLanes(){
        return laneAgent.getAllActiveLanes();
    }

    @PostMapping
    public ResponseDto createLane(@RequestBody LaneDto laneDto){
        return laneAgent.createLane(laneDto);
    }
}
