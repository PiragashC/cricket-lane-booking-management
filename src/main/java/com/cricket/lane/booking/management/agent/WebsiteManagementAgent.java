package com.cricket.lane.booking.management.agent;

import com.cricket.lane.booking.management.agent.converter.WebsiteManagementConverter;
import com.cricket.lane.booking.management.api.dto.ResponseDto;
import com.cricket.lane.booking.management.api.dto.WebsiteDto;
import com.cricket.lane.booking.management.entity.Website;
import com.cricket.lane.booking.management.exception.ServiceException;
import com.cricket.lane.booking.management.repository.WebsiteRepository;
import com.cricket.lane.booking.management.service.WebsiteManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebsiteManagementAgent {

    private final WebsiteManagementService websiteManagementService;

    private final WebsiteManagementConverter websiteManagementConverter;

    private final WebsiteRepository websiteRepository;

    public ResponseDto saveOrUpdateWebsite(WebsiteDto websiteDto) {
        Website website = null;

        if (websiteDto.getId() != null && !websiteDto.getId().isEmpty()) {
            Website existingWebsite = websiteRepository.findById(websiteDto.getId()).orElseThrow(() ->
                    new ServiceException("Website not found", "Bad request", HttpStatus.BAD_REQUEST));

            website = websiteManagementConverter.convertForUpdate(existingWebsite, websiteDto);
        } else {
            website = websiteManagementConverter.convertForSave(websiteDto);
        }

        return websiteManagementService.saveOrUpdateWebsite(website);
    }

    public WebsiteDto getWebsiteById(String id) {
        Website website = websiteManagementService.getWebsiteById(id);
        return websiteManagementConverter.convert(website);
    }

    public List<Map<String, String>> uploadImages(List<MultipartFile> images) throws IOException {
        return websiteManagementService.uploadImages(images);
    }

    public void deleteImageByDownloadUrl(String downloadUrl) throws IOException {
        websiteManagementService.deleteImageByUrl(downloadUrl);
    }
}
