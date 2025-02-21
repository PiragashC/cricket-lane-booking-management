package com.cricket.lane.booking.management.agent.converter;

import com.cricket.lane.booking.management.api.dto.CricketLaneBookingDto;
import com.cricket.lane.booking.management.entity.BookingDates;
import com.cricket.lane.booking.management.entity.CricketLaneBooking;
import com.cricket.lane.booking.management.entity.SelectedLanes;
import com.cricket.lane.booking.management.enums.BookingStatus;
import com.cricket.lane.booking.management.enums.BookingType;
import com.cricket.lane.booking.management.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cricket.lane.booking.management.constants.ApplicationConstants.BAD_REQUEST;

@Component
@RequiredArgsConstructor
public class BookingConverter {

    public CricketLaneBooking convert(CricketLaneBookingDto cricketLaneBookingDto) {
        CricketLaneBooking cricketLaneBooking = new CricketLaneBooking();

        Integer noOfLanes = cricketLaneBookingDto.getSelectedLanesDtos().size();

        Integer totalHours = cricketLaneBookingDto.getBookingDatesDtos().stream()
                .mapToInt(date -> calculateTotalHours(date, cricketLaneBookingDto.getFromTime(), cricketLaneBookingDto.getToTime()))
                .sum();

        Integer payment = totalHours * noOfLanes;
        BigDecimal totalPayment = BigDecimal.valueOf(payment).multiply(BigDecimal.valueOf(45.0));
        BigDecimal totalPriceWithTax = totalPayment.multiply(BigDecimal.valueOf(1.13));
        cricketLaneBooking.setBookingPrice(totalPriceWithTax);

        cricketLaneBooking.setId(cricketLaneBookingDto.getId());
        cricketLaneBooking.setBookingDetails(cricketLaneBookingDto.getBookingDetails());
        cricketLaneBooking.setBookingTitle(cricketLaneBookingDto.getBookingTitle());
        cricketLaneBooking.setEmail(cricketLaneBookingDto.getEmail());
        cricketLaneBooking.setFirstName(cricketLaneBookingDto.getFirstName());
        cricketLaneBooking.setLastName(cricketLaneBookingDto.getLastName());
        cricketLaneBooking.setFromTime(cricketLaneBookingDto.getFromTime());
        cricketLaneBooking.setToTime(cricketLaneBookingDto.getToTime());
        cricketLaneBooking.setTelephoneNumber(cricketLaneBookingDto.getTelephoneNumber());
        cricketLaneBooking.setOrganization(cricketLaneBookingDto.getOrganization());
        cricketLaneBooking.setBookingStatus(BookingStatus.PENDING);
        cricketLaneBooking.setCreatedDate(LocalDate.now());
        if (cricketLaneBookingDto.getBookingDatesDtos() != null) {
            cricketLaneBooking.setBookingDates(convertBookingDates(cricketLaneBookingDto.getBookingDatesDtos()));
        }

        if (cricketLaneBookingDto.getSelectedLanesDtos() != null) {
            cricketLaneBooking.setSelectedLanes(convertSelectedLanes(cricketLaneBookingDto.getSelectedLanesDtos()));
        }
        cricketLaneBooking.setBookingType(BookingType.fromMappedValue(cricketLaneBookingDto.getBookingType()));
        return cricketLaneBooking;
    }


    private int calculateTotalHours(LocalDate date, LocalTime fromTime, LocalTime toTime) {
        if (fromTime == null || toTime == null || date == null) {
            throw new ServiceException("Date, fromTime, and toTime cannot be null",BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }

        LocalDateTime startDateTime = LocalDateTime.of(date, fromTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, toTime);

        long minutes = Duration.between(startDateTime, endDateTime).toMinutes();

        return (int) Math.ceil(minutes / 60.0);
    }


    public Set<BookingDates> convertBookingDates(List<LocalDate> bookingDatesDtos) {
        Set<BookingDates> bookingDates = new HashSet<>();
        return bookingDatesDtos.stream()
                .map(bookingDatesDto -> {
                    BookingDates bookingDate = new BookingDates();
                    bookingDate.setBookingDate(bookingDatesDto);
                    bookingDates.add(bookingDate);

                    return bookingDate;
                }).collect(Collectors.toSet());
    }

    public Set<SelectedLanes> convertSelectedLanes(List<String> selectedLanesDtos) {
        Set<SelectedLanes> selectedLanes = new HashSet<>();
        return selectedLanesDtos.stream()
                .map(selectedLanesDto -> {
                    SelectedLanes selectedLane = new SelectedLanes();
                    selectedLane.setLaneId(selectedLanesDto);
                    selectedLanes.add(selectedLane);

                    return selectedLane;
                }).collect(Collectors.toSet());
    }
}
