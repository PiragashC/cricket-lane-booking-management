package com.cricket.lane.booking.management.service;

import com.cricket.lane.booking.management.api.dto.*;
import com.cricket.lane.booking.management.entity.CricketLaneBooking;
import com.cricket.lane.booking.management.enums.BookingStatus;
import com.cricket.lane.booking.management.exception.ServiceException;
import com.cricket.lane.booking.management.repository.CricketLaneBookingRepository;
import com.cricket.lane.booking.management.repository.LaneRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cricket.lane.booking.management.constants.ApplicationConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final CricketLaneBookingRepository cricketLaneBookingRepository;

    private final LaneRepository laneRepository;

    public ResponseDto bookingCricketLane(CricketLaneBooking cricketLaneBooking) {
        cricketLaneBooking.getBookingDates().stream()
                .map(bookingDate -> {
                    Boolean checkLaneFree = cricketLaneBookingRepository.checkLaneFree(cricketLaneBooking.getFromTime(), cricketLaneBooking.getToTime(), bookingDate.getBookingDate());
                    if (checkLaneFree.equals(Boolean.TRUE)) {
                            throw new ServiceException("Lane already booked on " +bookingDate.getBookingDate() +
                                    " from " + cricketLaneBooking.getFromTime() + " to " + cricketLaneBooking.getToTime(),BAD_REQUEST, HttpStatus.BAD_REQUEST);
                    }
                    return bookingDate;
                }).collect(Collectors.toSet());
        cricketLaneBooking.getSelectedLanes().stream()
                .map(selectedLane -> {
                    laneRepository.findById(selectedLane.getLaneId()).orElseThrow(() -> new ServiceException(LANE_NOT_FOUND,BAD_REQUEST,HttpStatus.BAD_REQUEST));
                    return selectedLane;
                }).collect(Collectors.toSet());
        CricketLaneBooking savedBooking = cricketLaneBookingRepository.save(cricketLaneBooking);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(BOOKING_SAVED);
        responseDto.setBookingId(savedBooking.getId());
        return responseDto;
    }

    public BookingPriceDto getBookingPrice(Integer noOfLanes, LocalTime fromTime, LocalTime toTime) {
        BookingPriceDto bookingPriceDto = new BookingPriceDto();
        double ratePerLane = 40.0;

        long durationMinutes = Duration.between(fromTime, toTime).toMinutes();

        long durationHours = (long) Math.ceil(durationMinutes / 60.0);

        BigDecimal totalPrice = BigDecimal.valueOf(noOfLanes * durationHours * ratePerLane);

        bookingPriceDto.setBookingPrice(totalPrice);

        return bookingPriceDto;
    }

    public List<AvailabilityResponseDto> checkLaneAvailability(List<String> laneId, LocalTime fromTime, LocalTime toTime, List<LocalDate> dates) {
        return dates.stream()
                .map(date -> {
                    Set<String> busyLanes = cricketLaneBookingRepository.checkLaneAvailability(fromTime, toTime, date);
                    List<LaneDto> availableLanes = new ArrayList<>();
                    laneId.stream()
                            .filter(lane -> !busyLanes.contains(lane))
                            .map(lane -> {
                                LaneDto laneDto = new LaneDto();
                                laneDto.setLaneId(lane);
                                String laneName = laneRepository.findLaneNameById(lane);
                                laneDto.setLaneName(laneName);
                                availableLanes.add(laneDto);
                                return laneDto;
                            })
                            .toList();

                    return new AvailabilityResponseDto(date, availableLanes);
                })
                .toList();
    }

    public CalenderResponseDto getAllBookingsForCalender(String laneId, LocalDate fromDate, LocalDate toDate) {
        List<BookingResponseDto> bookingResponseDtos = cricketLaneBookingRepository.getAllBookingsForCalender(laneId, fromDate, toDate);
        CalenderResponseDto calenderResponseDto = new CalenderResponseDto();
        String laneName = laneRepository.findLaneNameById(laneId);
        List<WeekMonthViewResponseDto> weekMonthViewResponseDtos = new ArrayList<>();
        calenderResponseDto.setLaneId(laneId);
        if (fromDate.equals(toDate)) {
            calenderResponseDto.setBookingDate(fromDate);
            calenderResponseDto.setLaneName(laneName);
            calenderResponseDto.setBookingResponseDtos(bookingResponseDtos);
        } else {
            List<LocalDate> dateList = getDatesBetween(fromDate, toDate);
            dateList.stream()
                    .map(date -> {
                        WeekMonthViewResponseDto weekMonthViewResponseDto = new WeekMonthViewResponseDto();
                        weekMonthViewResponseDto.setBookingDate(date);
                        weekMonthViewResponseDto.setBookingResponseDtos(bookingResponseDtos);
                        weekMonthViewResponseDtos.add(weekMonthViewResponseDto);
                        return date;
                    }).toList();
            calenderResponseDto.setLaneName(laneName);
            calenderResponseDto.setWeekMonthViewResponseDtos(weekMonthViewResponseDtos);
        }
        return calenderResponseDto;
    }

    private List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1)
                .toList();
    }

    @Transactional
    public ResponseDto updateStatus(String bookingId, String status) {
        CricketLaneBooking cricketLaneBooking = cricketLaneBookingRepository.findById(bookingId)
                .orElseThrow(() -> new ServiceException(BOOKING_ID_NOT_FOUND, BAD_REQUEST, HttpStatus.BAD_REQUEST));
        cricketLaneBooking.setBookingStatus(BookingStatus.fromMappedValue(status));
        cricketLaneBookingRepository.save(cricketLaneBooking);

        return new ResponseDto(BOOKING_STATUS_UPDATED);
    }


}
