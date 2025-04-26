package com.cricket.lane.booking.management.api.controller;

import com.cricket.lane.booking.management.agent.WebsiteManagementAgent;
import com.cricket.lane.booking.management.api.dto.ResponseDto;
import com.cricket.lane.booking.management.api.dto.WebsiteDto;
import com.cricket.lane.booking.management.entity.Website;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/upload")
    public ResponseEntity<List<Map<String, String>>> uploadImages(@RequestParam("images") List<MultipartFile> images) {
        try {
            List<Map<String, String>> response = websiteManagementAgent.uploadImages(images);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Collections.singletonList(Map.of("error", e.getMessage())));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteImage(@RequestParam("downloadUrl") String downloadUrl) {
        try {
            websiteManagementAgent.deleteImageByDownloadUrl(downloadUrl);
            return ResponseEntity.ok("Image deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete image: " + e.getMessage());
        }
    }

}
