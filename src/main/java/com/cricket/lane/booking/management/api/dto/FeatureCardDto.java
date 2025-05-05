package com.cricket.lane.booking.management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class FeatureCardDto {
    private String id;
    private String icon;
    private String iconViewUrl;
    private String iconDeleteUrl;
    private String name;
    private String description;
}
