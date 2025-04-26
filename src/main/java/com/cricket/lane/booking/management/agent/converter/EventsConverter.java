package com.cricket.lane.booking.management.agent.converter;

import com.cricket.lane.booking.management.api.dto.EventsDto;
import com.cricket.lane.booking.management.entity.Event;
import com.cricket.lane.booking.management.exception.ServiceException;
import com.cricket.lane.booking.management.repository.EventsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventsConverter {

    private final EventsRepository eventsRepository;

    @Value("${cloudinary.base-view-url}")
    private String cloudinaryBaseViewUrl;

    @Value("${cloudinary.base-download-url}")
    private String cloudinaryBaseDownloadUrl;

    public Event convert(EventsDto eventsDto) {
        if (eventsDto.getId() != null && !eventsDto.getId().isEmpty()) {
            Event existingEvent = eventsRepository.findById(eventsDto.getId())
                    .orElseThrow(() -> new ServiceException("Event not found", "Bad request", HttpStatus.BAD_REQUEST));
            existingEvent.setId(eventsDto.getId());
            existingEvent.setEventTitle(eventsDto.getEventTitle());
            existingEvent.setEventDate(eventsDto.getEventDate());
            existingEvent.setEventTime(eventsDto.getEventTime());
            existingEvent.setLocation(eventsDto.getLocation());
            existingEvent.setDescription(eventsDto.getDescription());
            existingEvent.setStatus(eventsDto.isStatus());
            existingEvent.setImage(eventsDto.getImage());
            return existingEvent;
        }

        return Event.builder()
                .id(eventsDto.getId())
                .eventTitle(eventsDto.getEventTitle())
                .eventDate(eventsDto.getEventDate())
                .eventTime(eventsDto.getEventTime())
                .location(eventsDto.getLocation())
                .description(eventsDto.getDescription())
                .status(eventsDto.isStatus())
                .image(eventsDto.getImage())
                .build();
    }

    public EventsDto convert(Event event) {

        return EventsDto.builder()
                .id(event.getId())
                .eventTitle(event.getEventTitle())
                .eventDate(event.getEventDate())
                .eventTime(event.getEventTime())
                .location(event.getLocation())
                .description(event.getDescription())
                .status(event.getStatus())
                .imageViewUrl(cloudinaryBaseViewUrl + event.getImage())
                .imageDeleteUrl(cloudinaryBaseDownloadUrl + event.getImage())
                .build();
    }
}
