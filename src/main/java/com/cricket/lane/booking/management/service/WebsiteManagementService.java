package com.cricket.lane.booking.management.service;

import com.cricket.lane.booking.management.api.dto.ResponseDto;
import com.cricket.lane.booking.management.entity.Website;
import com.cricket.lane.booking.management.exception.ServiceException;
import com.cricket.lane.booking.management.repository.WebsiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebsiteManagementService {

    private final WebsiteRepository websiteRepository;

    public ResponseDto saveOrUpdateWebsite(Website website) {

        websiteRepository.save(website);

        return new ResponseDto("Website saved");
    }

    public Website getWebsiteById(String id){
        return websiteRepository.findById(id).orElseThrow(() ->
                new ServiceException("Website not found", "Bad request", HttpStatus.BAD_REQUEST));
    }
}
