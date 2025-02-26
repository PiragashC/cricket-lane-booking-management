package com.cricket.lane.booking.management.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class DropDownDto {
    private List<String> bookingStatus;
    private List<String> bookingType;
}
