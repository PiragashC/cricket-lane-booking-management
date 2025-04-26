package com.cricket.lane.booking.management.api.controller;

import com.cricket.lane.booking.management.agent.WebsiteManagementAgent;
import com.cricket.lane.booking.management.api.dto.ResponseDto;
import com.cricket.lane.booking.management.api.dto.WebsiteDto;
import com.cricket.lane.booking.management.entity.Website;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/website")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://sports-booking-hub.onrender.com","https://koverdrivesports.ca","https://kover-drive.onrender.com"})
public class WebsiteManagementController {

    private final WebsiteManagementAgent websiteManagementAgent;

    @PostMapping
    public ResponseDto saveOrUpdateWebsite(@RequestBody WebsiteDto websiteDto){
        return websiteManagementAgent.saveOrUpdateWebsite(websiteDto);
    }

    @GetMapping
    public WebsiteDto getWebsiteById(@RequestParam(value = "id") String id){
        return websiteManagementAgent.getWebsiteById(id);
    }

}
