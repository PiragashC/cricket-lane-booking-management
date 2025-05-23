package com.cricket.lane.booking.management.agent.converter;

import com.cricket.lane.booking.management.api.dto.BookingDatesDto;
import com.cricket.lane.booking.management.api.dto.CricketLaneBookingDto;
import com.cricket.lane.booking.management.api.dto.LaneDto;
import com.cricket.lane.booking.management.entity.BookingDates;
import com.cricket.lane.booking.management.entity.CricketLaneBooking;
import com.cricket.lane.booking.management.entity.PromoCode;
import com.cricket.lane.booking.management.entity.SelectedLanes;
import com.cricket.lane.booking.management.enums.BookingStatus;
import com.cricket.lane.booking.management.enums.BookingType;
import com.cricket.lane.booking.management.exception.ServiceException;
import com.cricket.lane.booking.management.repository.LaneRepository;
import com.cricket.lane.booking.management.repository.PromoCodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cricket.lane.booking.management.constants.ApplicationConstants.BAD_REQUEST;

@Component
@RequiredArgsConstructor
public class BookingConverter {

    private final LaneRepository laneRepository;
    private final PromoCodeRepository promoCodeRepository;

    public CricketLaneBooking convert(CricketLaneBookingDto cricketLaneBookingDto) {
        CricketLaneBooking cricketLaneBooking = new CricketLaneBooking();

        Integer totalHours = cricketLaneBookingDto.getBookingDatesDtos().stream()
                .mapToInt(date -> calculateTotalHours(date, cricketLaneBookingDto.getFromTime(), cricketLaneBookingDto.getToTime()))
                .sum();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (String laneId : cricketLaneBookingDto.getSelectedLanesDtos()) {
            BigDecimal lanePrice = laneRepository.findLanePrice(laneId);
            BigDecimal laneTotal = lanePrice.multiply(BigDecimal.valueOf(totalHours));
            totalPrice = totalPrice.add(laneTotal);
        }

        BigDecimal totalPriceWithTax = totalPrice.multiply(BigDecimal.valueOf(1.13));

        PromoCode koverDrivePromoCode = promoCodeRepository.getPromoCodeToCalculatePrice();

        if (koverDrivePromoCode.getIsActive().equals(Boolean.TRUE)) {
            if (koverDrivePromoCode != null && koverDrivePromoCode.getPromoCode().equals(cricketLaneBookingDto.getPromoCode())) {
                BigDecimal discountPercentage = koverDrivePromoCode.getDiscount();
                BigDecimal discountAmount = totalPriceWithTax.multiply(discountPercentage.divide(BigDecimal.valueOf(100)));
                totalPriceWithTax = totalPriceWithTax.subtract(discountAmount);
            }
        }

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
        cricketLaneBooking.setCreatedDate(LocalDate.now());

        if (cricketLaneBookingDto.getBookingDatesDtos() != null) {
            cricketLaneBooking.setBookingDates(convertBookingDates(cricketLaneBookingDto.getBookingDatesDtos()));
        }

        if (cricketLaneBookingDto.getSelectedLanesDtos() != null) {
            cricketLaneBooking.setSelectedLanes(convertSelectedLanes(cricketLaneBookingDto.getSelectedLanesDtos()));
        }

        cricketLaneBooking.setBookingType(BookingType.fromMappedValue(cricketLaneBookingDto.getBookingType()));

        if (cricketLaneBookingDto.getBookingType().equalsIgnoreCase(BookingType.OFFLINE.getMappedValue())) {
            cricketLaneBooking.setBookingStatus(BookingStatus.SUCCESS);
        } else {
            cricketLaneBooking.setBookingStatus(BookingStatus.PENDING);
        }

        return cricketLaneBooking;
    }

    public CricketLaneBookingDto convert(CricketLaneBooking cricketLaneBooking) {
        CricketLaneBookingDto cricketLaneBookingDto = new CricketLaneBookingDto();

        cricketLaneBookingDto.setBookingPrice(cricketLaneBooking.getBookingPrice());

        cricketLaneBookingDto.setId(cricketLaneBooking.getId());
        cricketLaneBookingDto.setBookingDetails(cricketLaneBooking.getBookingDetails());
        cricketLaneBookingDto.setBookingTitle(cricketLaneBooking.getBookingTitle());
        cricketLaneBookingDto.setEmail(cricketLaneBooking.getEmail());
        cricketLaneBookingDto.setFirstName(cricketLaneBooking.getFirstName());
        cricketLaneBookingDto.setLastName(cricketLaneBooking.getLastName());
        cricketLaneBookingDto.setFromTime(cricketLaneBooking.getFromTime());
        cricketLaneBookingDto.setToTime(cricketLaneBooking.getToTime());
        cricketLaneBookingDto.setTelephoneNumber(cricketLaneBooking.getTelephoneNumber());
        cricketLaneBookingDto.setOrganization(cricketLaneBooking.getOrganization());
        cricketLaneBookingDto.setBookingStatus(cricketLaneBooking.getBookingStatus().getMappedValue());
        if (cricketLaneBooking.getBookingDates() != null) {
            cricketLaneBookingDto.setBookingDatesDtos(convertBookingDatesDto(cricketLaneBooking.getBookingDates()));
        }

        if (cricketLaneBooking.getSelectedLanes() != null) {
            cricketLaneBookingDto.setLaneDtos(convertSelectedLanesDto(cricketLaneBooking.getSelectedLanes()));
        }
//        cricketLaneBookingDto.setBookingType(cricketLaneBooking.getBookingType().getMappedValue());
        return cricketLaneBookingDto;
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

    public List<LocalDate> convertBookingDatesDto(Set<BookingDates> bookingDates) {
        return bookingDates.stream()
                .map(BookingDates::getBookingDate)
                .toList();
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

    public List<LaneDto> convertSelectedLanesDto(Set<SelectedLanes> selectedLanes) {
        List<LaneDto> laneDtos = new ArrayList<>();
        return selectedLanes.stream()
                .map(selectedLane -> {
                    LaneDto laneDto = new LaneDto();
                    String laneName = laneRepository.findLaneNameById(selectedLane.getLaneId());
                    laneDto.setLaneId(selectedLane.getLaneId());
                    laneDto.setLaneName(laneName);
                    laneDtos.add(laneDto);
                    return laneDto;
                })
                .toList();
    }

    @Transactional
    public CricketLaneBooking convertForUpdate(CricketLaneBooking existingBooking, CricketLaneBookingDto cricketLaneBookingDto) {
        existingBooking.setId(cricketLaneBookingDto.getId());
        existingBooking.setBookingDetails(cricketLaneBookingDto.getBookingDetails());
        existingBooking.setBookingTitle(cricketLaneBookingDto.getBookingTitle());
        existingBooking.setEmail(cricketLaneBookingDto.getEmail());
        existingBooking.setFirstName(cricketLaneBookingDto.getFirstName());
        existingBooking.setLastName(cricketLaneBookingDto.getLastName());
        existingBooking.setFromTime(cricketLaneBookingDto.getFromTime());
        existingBooking.setToTime(cricketLaneBookingDto.getToTime());
        existingBooking.setTelephoneNumber(cricketLaneBookingDto.getTelephoneNumber());
        existingBooking.setOrganization(cricketLaneBookingDto.getOrganization());
        existingBooking.setBookingStatus(BookingStatus.PENDING);
        existingBooking.setCreatedDate(LocalDate.now());
        if (cricketLaneBookingDto.getBookingDatesDtos() != null) {
            existingBooking.setBookingDates(convertBookingDates(cricketLaneBookingDto.getBookingDatesDtos()));
        }

        if (cricketLaneBookingDto.getSelectedLanesDtos() != null) {
            existingBooking.setSelectedLanes(convertSelectedLanes(cricketLaneBookingDto.getSelectedLanesDtos()));
        }
        existingBooking.setBookingType(BookingType.fromMappedValue(cricketLaneBookingDto.getBookingType()));
        existingBooking.setBookingPrice(cricketLaneBookingDto.getBookingPrice());
        return existingBooking;
    }
}
