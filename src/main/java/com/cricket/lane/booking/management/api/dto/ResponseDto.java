package com.cricket.lane.booking.management.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {
    private String message;
    private String bookingId;

    public ResponseDto(String message) {
        this.message = message;
    }
}
