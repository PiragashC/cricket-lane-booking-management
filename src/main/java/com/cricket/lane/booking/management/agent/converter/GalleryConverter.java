package com.cricket.lane.booking.management.agent.converter;

import com.cricket.lane.booking.management.api.dto.GalleryDto;
import com.cricket.lane.booking.management.entity.Gallery;
import com.cricket.lane.booking.management.exception.ServiceException;
import com.cricket.lane.booking.management.repository.GalleryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GalleryConverter {

    private final GalleryRepository galleryRepository;

    @Value("${cloudinary.base-view-url}")
    private String cloudinaryBaseViewUrl;

    @Value("${cloudinary.base-download-url}")
    private String cloudinaryBaseDownloadUrl;

    public Gallery convert(GalleryDto galleryDto) {
        if (galleryDto.getId() != null && !galleryDto.getId().isEmpty()) {
            Gallery existingGallery = galleryRepository.findById(galleryDto.getId())
                    .orElseThrow(() -> new ServiceException("Gallery not found", "Bad request", HttpStatus.BAD_REQUEST));
            existingGallery.setId(galleryDto.getId());
            existingGallery.setTitle(galleryDto.getTitle());
            existingGallery.setImage(galleryDto.getImage());
            existingGallery.setUploadedDate(galleryDto.getUploadedDate());
            existingGallery.setStatus(galleryDto.isStatus());
            return existingGallery;
        }

        return Gallery.builder()
                .id(galleryDto.getId())
                .title(galleryDto.getTitle())
                .image(galleryDto.getImage())
                .uploadedDate(galleryDto.getUploadedDate())
                .status(galleryDto.isStatus())
                .build();
    }

    public GalleryDto convert(Gallery gallery) {

        return GalleryDto.builder()
                .id(gallery.getId())
                .title(gallery.getTitle())
                .image(gallery.getImage())
                .imageViewUrl(cloudinaryBaseViewUrl + gallery.getImage())
                .imageDeleteUrl(cloudinaryBaseDownloadUrl + gallery.getImage())
                .uploadedDate(gallery.getUploadedDate())
                .status(gallery.getStatus())
                .build();
    }

}
