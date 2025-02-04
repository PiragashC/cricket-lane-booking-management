package com.cricket.lane.booking.management.agent.converter;

import com.cricket.lane.booking.management.api.dto.BookingDatesDto;
import com.cricket.lane.booking.management.api.dto.CricketLaneBookingDto;
import com.cricket.lane.booking.management.api.dto.SelectedLanesDto;
import com.cricket.lane.booking.management.entity.BookingDates;
import com.cricket.lane.booking.management.entity.CricketLaneBooking;
import com.cricket.lane.booking.management.entity.SelectedLanes;
import com.cricket.lane.booking.management.enums.BookingStatus;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookingConverter {

    public CricketLaneBooking convert(CricketLaneBookingDto cricketLaneBookingDto){
        return CricketLaneBooking.builder()
                .id(cricketLaneBookingDto.getId())
                .bookingDetails(cricketLaneBookingDto.getBookingDetails())
                .bookingTitle(cricketLaneBookingDto.getBookingTitle())
                .email(cricketLaneBookingDto.getEmail())
                .bookingPrice(cricketLaneBookingDto.getBookingPrice())
                .firstName(cricketLaneBookingDto.getFirstName())
                .lastName(cricketLaneBookingDto.getLastName())
                .fromTime(cricketLaneBookingDto.getFromTime())
                .toTime(cricketLaneBookingDto.getToTime())
                .telephoneNumber(cricketLaneBookingDto.getTelephoneNumber())
                .organization(cricketLaneBookingDto.getOrganization())
                .bookingDates(cricketLaneBookingDto.getBookingDatesDtos() != null ?
                        convertBookingDates(cricketLaneBookingDto.getBookingDatesDtos()) : null)
                .selectedLanes(cricketLaneBookingDto.getSelectedLanesDtos() != null ?
                        convertSelectedLanes(cricketLaneBookingDto.getSelectedLanesDtos()) : null)
                .bookingStatus(BookingStatus.PENDING)
                .build();
    }

    public Set<BookingDates> convertBookingDates(Set<BookingDatesDto> bookingDatesDtos){
        Set<BookingDates> bookingDates = new HashSet<>();
        return bookingDatesDtos.stream()
                .map(bookingDatesDto -> {
                    BookingDates bookingDate = new BookingDates();
                    bookingDate.setId(bookingDatesDto.getId());
                    bookingDate.setBookingDate(bookingDatesDto.getBookingDate());
                    bookingDate.setCricketLaneBookingId(bookingDatesDto.getCricketLaneBookingId());
                    bookingDates.add(bookingDate);

                    return bookingDate;
                }).collect(Collectors.toSet());
    }

    public Set<SelectedLanes> convertSelectedLanes(Set<SelectedLanesDto> selectedLanesDtos){
        Set<SelectedLanes> selectedLanes = new HashSet<>();
        return selectedLanesDtos.stream()
                .map(selectedLanesDto -> {
                    SelectedLanes selectedLane = new SelectedLanes();
                    selectedLane.setId(selectedLanesDto.getId());
                    selectedLane.setLaneId(selectedLanesDto.getLaneId());
                    selectedLane.setCricketLaneBookingId(selectedLanesDto.getCricketLaneBookingId());
                    selectedLanes.add(selectedLane);

                    return selectedLane;
                }).collect(Collectors.toSet());
    }
}
