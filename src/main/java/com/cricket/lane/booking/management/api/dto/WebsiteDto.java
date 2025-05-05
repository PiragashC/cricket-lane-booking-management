package com.cricket.lane.booking.management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class WebsiteDto {
    private String id;
    private String contentOne;
    private String contentTwo;
    private List<LaneCardDto> contentThree;
    private String contentFour;
    private String contentFourViewUrl;
    private String contentFourDeleteUrl;
    private String contentFive;
    private String contentSix;
    private String contentSeven;
    private String contentEight;
    private String contentNine;
    private String contentTen;
    private String contentTenViewUrl;
    private String contentTenDeleteUrl;
    private String contentEleven;
    private List<FeatureCardDto> contentTwelve;
    private List<GalleryDto> contentThirteen;
    private List<EventsDto> contentFourteen;
}
