package com.cricket.lane.booking.management.service;

import com.cricket.lane.booking.management.agent.converter.LaneConverter;
import com.cricket.lane.booking.management.api.dto.BookingDto;
import com.cricket.lane.booking.management.api.dto.LaneDto;
import com.cricket.lane.booking.management.api.dto.ResponseDto;
import com.cricket.lane.booking.management.entity.Lanes;
import com.cricket.lane.booking.management.exception.ServiceException;
import com.cricket.lane.booking.management.repository.LaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.cricket.lane.booking.management.constants.ApplicationConstants.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class LaneService {

    private final LaneRepository laneRepository;

    private final LaneConverter laneConverter;

    public List<LaneDto> getAllActiveLanes() {
        return laneRepository.getAllActiveLanes();
    }

    public ResponseDto createLane(Lanes lanes) {
        laneRepository.save(lanes);

        return new ResponseDto("Lane created");
    }

    public ResponseDto updateLaneStatus(String id, boolean status) {
        Lanes lanes = laneRepository.findById(id).orElseThrow(() ->
                new ServiceException("Lane not found",BAD_REQUEST, HttpStatus.BAD_REQUEST));
        lanes.setIsActive(status);
        laneRepository.save(lanes);

        return new ResponseDto("Lane status updated");
    }

    public LaneDto getById(String id) {
        return laneRepository.getLaneById(id);
    }

    public ResponseDto updateLane(LaneDto laneDto) {
        Lanes existingLane = laneRepository.findById(laneDto.getLaneId()).orElseThrow(() ->
                new ServiceException("Lane not found",BAD_REQUEST, HttpStatus.BAD_REQUEST));

        laneRepository.save(laneConverter.convert(existingLane,laneDto));

        return new ResponseDto("Lane details updated");
    }

    public Page<LaneDto> getAllLanes(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        LaneDto.resetCounter();
        return laneRepository.geAllLanes(pageable);
    }
}
