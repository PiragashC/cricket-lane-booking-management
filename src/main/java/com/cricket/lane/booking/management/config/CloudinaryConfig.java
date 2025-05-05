package com.cricket.lane.booking.management.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dwx7x0vxr",
                "api_key", "471522816547322",
                "api_secret", "9HS0QofFKbgxqTmKJEffO87JA-4",
                "secure", true
        ));
    }
}
