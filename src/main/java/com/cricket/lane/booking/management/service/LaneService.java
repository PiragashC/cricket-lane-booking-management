package com.cricket.lane.booking.management.service;

import com.cricket.lane.booking.management.api.dto.LaneDto;
import com.cricket.lane.booking.management.api.dto.ResponseDto;
import com.cricket.lane.booking.management.entity.Lanes;
import com.cricket.lane.booking.management.repository.LaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LaneService {

    private final LaneRepository laneRepository;

    public List<LaneDto> getAllActiveLanes() {
        return laneRepository.getAllActiveLanes();
    }

    public ResponseDto createLane(Lanes lanes) {
        laneRepository.save(lanes);

        return new ResponseDto("Lane craeted");
    }
}
