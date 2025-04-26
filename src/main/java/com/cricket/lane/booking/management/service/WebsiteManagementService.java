package com.cricket.lane.booking.management.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cricket.lane.booking.management.api.dto.ResponseDto;
import com.cricket.lane.booking.management.entity.Website;
import com.cricket.lane.booking.management.exception.ServiceException;
import com.cricket.lane.booking.management.repository.WebsiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebsiteManagementService {

    private final WebsiteRepository websiteRepository;

    private final Cloudinary cloudinary;


    public ResponseDto saveOrUpdateWebsite(Website website) {

        websiteRepository.save(website);

        return new ResponseDto("Website saved");
    }

    public Website getWebsiteById(String id){
        return websiteRepository.findById(id).orElseThrow(() ->
                new ServiceException("Website not found", "Bad request", HttpStatus.BAD_REQUEST));
    }

    public List<Map<String, String>> uploadImages(List<MultipartFile> images) throws IOException {
        List<Map<String, String>> uploadedPaths = new ArrayList<>();

        for (MultipartFile image : images) {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto",
                    "use_filename", true,
                    "public_id", "custom_" + System.currentTimeMillis()
            ));


            String publicId = (String) uploadResult.get("public_id");
            String version = uploadResult.get("version").toString();
            String format = (String) uploadResult.get("format");

            String savedPath = "v" + version + "/" + publicId + "." + format;

            Map<String, String> filePaths = new HashMap<>();
            filePaths.put("path", savedPath);
            filePaths.put("public_id", publicId);
            uploadedPaths.add(filePaths);
        }

        return uploadedPaths;
    }


    public void deleteImageByUrl(String cloudinaryUrl) throws IOException {
        String uploadPrefix = "/upload/";
        int startIndex = cloudinaryUrl.indexOf(uploadPrefix);
        if (startIndex == -1) {
            throw new IllegalArgumentException("Invalid Cloudinary URL: " + cloudinaryUrl);
        }

        String relativePath = cloudinaryUrl.substring(startIndex + uploadPrefix.length());

        // Remove "fl_attachment/" if present
        if (relativePath.startsWith("fl_attachment/")) {
            relativePath = relativePath.substring("fl_attachment/".length());
        }

        // Example: v1234567890/custom_1234567890.png
        String[] parts = relativePath.split("/");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid Cloudinary path format.");
        }

        // Get filename without extension → public_id
        String fileName = parts[1]; // custom_1745658355889.png
        String publicId = fileName.substring(0, fileName.lastIndexOf('.'));
        log.info("public iddd---{}",publicId);
        // Now delete using the extracted public_id
        deleteImageByPublicId(publicId);
    }

    public void deleteImageByPublicId(String publicId) throws IOException {
        // Directly use the public_id to delete the image
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        if (!"ok".equals(result.get("result"))) {
            throw new IOException("Cloudinary failed to delete the image. Response: " + result);
        }

        System.out.println("Image deleted successfully: " + publicId);
    }

}
