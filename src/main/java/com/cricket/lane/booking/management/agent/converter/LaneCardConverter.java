package com.cricket.lane.booking.management.agent.converter;

import com.cricket.lane.booking.management.api.dto.LaneCardDto;
import com.cricket.lane.booking.management.entity.LaneCard;
import com.cricket.lane.booking.management.exception.ServiceException;
import com.cricket.lane.booking.management.repository.LaneCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LaneCardConverter {

    private final LaneCardRepository laneCardRepository;

    public LaneCard convert(LaneCardDto laneCardDto) {
        if (laneCardDto.getId() != null && !laneCardDto.getId().isEmpty()) {
            LaneCard existingLaneCard = laneCardRepository.findById(laneCardDto.getId())
                    .orElseThrow(() -> new ServiceException("Lane card not found","Bad request", HttpStatus.BAD_REQUEST));
            existingLaneCard.setId(laneCardDto.getId());
            existingLaneCard.setLaneCardTitle(laneCardDto.getLaneCardTitle());
            existingLaneCard.setFrequency(laneCardDto.getFrequency());
            existingLaneCard.setTimeInterval(laneCardDto.getTimeInterval());
            existingLaneCard.setRatePerHour(laneCardDto.getRatePerHour());

            return existingLaneCard;
        }

        return LaneCard.builder()
                .id(laneCardDto.getId())
                .laneCardTitle(laneCardDto.getLaneCardTitle())
                .frequency(laneCardDto.getFrequency())
                .timeInterval(laneCardDto.getTimeInterval())
                .ratePerHour(laneCardDto.getRatePerHour())
                .build();
    }

    public LaneCardDto convert(LaneCard laneCard) {
        return LaneCardDto.builder()
                .id(laneCard.getId())
                .laneCardTitle(laneCard.getLaneCardTitle())
                .frequency(laneCard.getFrequency())
                .timeInterval(laneCard.getTimeInterval())
                .ratePerHour(laneCard.getRatePerHour())
                .build();
    }
}
