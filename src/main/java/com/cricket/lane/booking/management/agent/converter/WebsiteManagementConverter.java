package com.cricket.lane.booking.management.agent.converter;

import com.cricket.lane.booking.management.api.dto.WebsiteDto;
import com.cricket.lane.booking.management.entity.Website;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WebsiteManagementConverter {

    private final GalleryConverter galleryConverter;

    private final FeatureCardConverter featureCardConverter;

    private final LaneCardConverter laneCardConverter;

    private final EventsConverter eventsConverter;

    public Website convertForUpdate(Website existingWebsite, WebsiteDto websiteDto) {
        existingWebsite.setId(websiteDto.getId() != null ? websiteDto.getId() : existingWebsite.getId());
        existingWebsite.setContentOne(websiteDto.getContentOne() != null ? websiteDto.getContentOne() : existingWebsite.getContentOne());
        existingWebsite.setContentTwo(websiteDto.getContentTwo() != null ? websiteDto.getContentTwo() : existingWebsite.getContentTwo());
        existingWebsite.setContentThree(websiteDto.getContentThree() != null ?
                websiteDto.getContentThree().stream()
                        .map(laneCard -> laneCardConverter.convert(laneCard))
                        .collect(Collectors.toSet()) : existingWebsite.getContentThree());
        existingWebsite.setContentFour(websiteDto.getContentFour() != null ? websiteDto.getContentFour() : existingWebsite.getContentFour());
        existingWebsite.setContentFive(websiteDto.getContentFive() != null ? websiteDto.getContentFive() : existingWebsite.getContentFive());
        existingWebsite.setContentSix(websiteDto.getContentSix() != null ? websiteDto.getContentSix() : existingWebsite.getContentSix());
        existingWebsite.setContentSeven(websiteDto.getContentSeven() != null ? websiteDto.getContentSeven() : existingWebsite.getContentSeven());
        existingWebsite.setContentEight(websiteDto.getContentEight() != null ? websiteDto.getContentEight() : existingWebsite.getContentEight());
        existingWebsite.setContentNine(websiteDto.getContentNine() != null ? websiteDto.getContentNine() : existingWebsite.getContentNine());
        existingWebsite.setContentTen(websiteDto.getContentTen() != null ? websiteDto.getContentTen() : existingWebsite.getContentTen());
        existingWebsite.setContentEleven(websiteDto.getContentEleven() != null ? websiteDto.getContentEleven() : existingWebsite.getContentEleven());
        existingWebsite.setContentTwelve(websiteDto.getContentTwelve() != null ?
                websiteDto.getContentTwelve().stream()
                        .map(featureCardDto -> featureCardConverter.convert(featureCardDto))
                        .collect(Collectors.toSet()) : existingWebsite.getContentTwelve());
        existingWebsite.setContentThirteen(websiteDto.getContentThirteen() != null ?
                websiteDto.getContentThirteen().stream()
                        .map(galleryDto -> galleryConverter.convert(galleryDto))
                        .collect(Collectors.toSet()) : existingWebsite.getContentThirteen());
        existingWebsite.setContentFourteen(websiteDto.getContentFourteen() != null ?
                websiteDto.getContentFourteen().stream()
                        .map(eventsDto -> eventsConverter.convert(eventsDto))
                        .collect(Collectors.toSet()) : existingWebsite.getContentFourteen());
        return existingWebsite;
    }


    public Website convertForSave(WebsiteDto websiteDto) {
        return Website.builder()
                .id(websiteDto.getId())
                .contentOne(websiteDto.getContentOne())
                .contentTwo(websiteDto.getContentTwo())
                .contentThree(websiteDto.getContentThree() != null ?
                        websiteDto.getContentThree().stream()
                                .map(laneCardConverter::convert)
                                .collect(Collectors.toSet()) : null)
                .contentFour(websiteDto.getContentFour())
                .contentFive(websiteDto.getContentFive())
                .contentSix(websiteDto.getContentSix())
                .contentSeven(websiteDto.getContentSeven())
                .contentEight(websiteDto.getContentEight())
                .contentNine(websiteDto.getContentNine())
                .contentTen(websiteDto.getContentTen())
                .contentEleven(websiteDto.getContentEleven())
                .contentTwelve(websiteDto.getContentTwelve() != null ?
                        websiteDto.getContentTwelve().stream()
                                .map(featureCardConverter::convert)
                                .collect(Collectors.toSet()) : null)
                .contentThirteen(websiteDto.getContentThirteen() != null ?
                        websiteDto.getContentThirteen().stream()
                                .map(galleryConverter::convert)
                                .collect(Collectors.toSet()) : null)
                .contentFourteen(websiteDto.getContentFourteen() != null ?
                        websiteDto.getContentFourteen().stream()
                                .map(eventsConverter::convert)
                                .collect(Collectors.toSet()) : null)
                .build();
    }

    public WebsiteDto convert(Website website) {
        return WebsiteDto.builder()
                .id(website.getId())
                .contentOne(website.getContentOne())
                .contentTwo(website.getContentTwo())
                .contentThree(website.getContentThree() != null ?
                        website.getContentThree().stream()
                                .map(laneCardConverter::convert)
                                .toList() : null)
                .contentFour(website.getContentFour())
                .contentFive(website.getContentFive())
                .contentSix(website.getContentSix())
                .contentSeven(website.getContentSeven())
                .contentEight(website.getContentEight())
                .contentNine(website.getContentNine())
                .contentTen(website.getContentTen())
                .contentEleven(website.getContentEleven())
                .contentTwelve(website.getContentTwelve() != null ?
                        website.getContentTwelve().stream()
                                .map(featureCardConverter::convert)
                                .toList() : null)
                .contentThirteen(website.getContentThirteen() != null ?
                        website.getContentThirteen().stream()
                                .map(galleryConverter::convert)
                                .toList() : null)
                .contentFourteen(website.getContentFourteen() != null ?
                        website.getContentFourteen().stream()
                                .map(eventsConverter::convert)
                                .toList() : null)
                .build();
    }
}
