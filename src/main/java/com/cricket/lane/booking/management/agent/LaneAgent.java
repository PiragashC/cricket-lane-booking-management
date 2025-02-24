package com.cricket.lane.booking.management.agent;

import com.cricket.lane.booking.management.agent.converter.LaneConverter;
import com.cricket.lane.booking.management.api.dto.LaneDto;
import com.cricket.lane.booking.management.api.dto.PaginatedResponseDto;
import com.cricket.lane.booking.management.api.dto.ResponseDto;
import com.cricket.lane.booking.management.entity.Lanes;
import com.cricket.lane.booking.management.service.LaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    public LaneDto getById(String id) {
        return laneService.getById(id);
    }

    public ResponseDto updateLane(LaneDto laneDto) {
        return laneService.updateLane(laneDto);
    }

    public PaginatedResponseDto<LaneDto> getAllLanes(int page, int size) {
        Page<LaneDto> laneDtos = laneService.getAllLanes(page,size);
        List<LaneDto> laneDtoList = laneDtos.getContent();

        PaginatedResponseDto<LaneDto> paginatedResponseDto = new PaginatedResponseDto<>();
        paginatedResponseDto.setData(laneDtoList);
        paginatedResponseDto.setTotalItems(laneDtos.getTotalElements());
        paginatedResponseDto.setTotalPages(laneDtos.getTotalPages());
        paginatedResponseDto.setCurrentPage(page);

        return paginatedResponseDto;
    }
}
