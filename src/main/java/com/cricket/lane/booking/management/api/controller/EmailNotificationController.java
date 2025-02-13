package com.cricket.lane.booking.management.api.controller;

import com.cricket.lane.booking.management.api.dto.EmailDataDto;
import com.cricket.lane.booking.management.service.EmailNotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/emails")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class EmailNotificationController {

    private final EmailNotificationService emailNotificationService;


    @PostMapping
    public void send(@RequestBody EmailDataDto dto) {
        emailNotificationService.send(dto);
    }
}
