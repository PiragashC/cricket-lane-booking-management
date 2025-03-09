package com.cricket.lane.booking.management.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaneDto {
    private static final AtomicInteger counter = new AtomicInteger(1);

    private String laneId;
    private String laneName;
    private Boolean isActive;
    private String laneNumber;
    private BigDecimal lanePrice;

    public LaneDto(String id, String laneName,BigDecimal lanePrice) {
        this.laneId = id;
        this.laneName = laneName;
        this.lanePrice = lanePrice;
    }

    public LaneDto(String laneId, String laneName, Boolean isActive,BigDecimal lanePrice) {
        this.laneId = laneId;
        this.laneName = laneName;
        this.isActive = isActive;
        this.laneNumber = generateLaneNumber();
        this.lanePrice = lanePrice;
    }

    private String generateLaneNumber() {
        return String.format("L%06d", counter.getAndIncrement());
    }

    public static void resetCounter() {
        counter.set(1);
    }
}
