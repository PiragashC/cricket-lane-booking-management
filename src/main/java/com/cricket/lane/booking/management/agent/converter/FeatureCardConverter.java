package com.cricket.lane.booking.management.agent.converter;

import com.cricket.lane.booking.management.api.dto.FeatureCardDto;
import com.cricket.lane.booking.management.entity.FeatureCard;
import com.cricket.lane.booking.management.exception.ServiceException;
import com.cricket.lane.booking.management.repository.FeatureCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeatureCardConverter {

    private final FeatureCardRepository featureCardRepository;

    @Value("${cloudinary.base-view-url}")
    private String cloudinaryBaseViewUrl;

    @Value("${cloudinary.base-download-url}")
    private String cloudinaryBaseDownloadUrl;

    public FeatureCard convert(FeatureCardDto featureCardDto) {
        if (featureCardDto.getId() != null && !featureCardDto.getId().isEmpty()) {
            FeatureCard existingFeatureCard = featureCardRepository.findById(featureCardDto.getId())
                    .orElseThrow(() -> new ServiceException("Feature card not found", "Bad request", HttpStatus.BAD_REQUEST));
            existingFeatureCard.setId(featureCardDto.getId());
            existingFeatureCard.setIcon(featureCardDto.getIcon());
            existingFeatureCard.setName(featureCardDto.getName());
            existingFeatureCard.setDescription(featureCardDto.getDescription());
            return existingFeatureCard;
        }

        return FeatureCard.builder()
                .id(featureCardDto.getId())
                .icon(featureCardDto.getIcon())
                .name(featureCardDto.getName())
                .description(featureCardDto.getDescription())
                .build();
    }

    public FeatureCardDto convert(FeatureCard featureCard) {
        return FeatureCardDto.builder()
                .id(featureCard.getId())
                .icon(featureCard.getIcon())
                .iconViewUrl(cloudinaryBaseViewUrl + featureCard.getIcon())
                .iconDeleteUrl(cloudinaryBaseDownloadUrl + featureCard.getIcon())
                .name(featureCard.getName())
                .description(featureCard.getDescription())
                .build();
    }
}
