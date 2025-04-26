package com.cricket.lane.booking.management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class GalleryDto {
    private String id;
    private String title;
    private String image;
    private String imageViewUrl;
    private String imageDeleteUrl;
    private LocalDate uploadedDate;
    private boolean status;
}
