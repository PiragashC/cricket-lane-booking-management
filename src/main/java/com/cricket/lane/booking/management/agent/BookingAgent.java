package com.cricket.lane.booking.management.agent;

import com.cricket.lane.booking.management.agent.converter.BookingConverter;
import com.cricket.lane.booking.management.api.dto.*;
import com.cricket.lane.booking.management.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingAgent {

    private final BookingService bookingService;

    private final BookingConverter bookingConverter;

    public ResponseDto bookingCricketLane(CricketLaneBookingDto cricketLaneBookingDto){
        return bookingService.bookingCricketLane(bookingConverter.convert(cricketLaneBookingDto));
    }

    public BookingPriceDto getBookingPrice(Integer noOfLanes, LocalTime fromTime, LocalTime toTime) {
        return bookingService.getBookingPrice(noOfLanes,fromTime,toTime);
    }

    public List<LaneDto> checkLaneAvailability(LocalTime fromTime, LocalTime toTime, List<LocalDate> date) {
        return bookingService.checkLaneAvailability(fromTime,toTime,date);
    }

    public CalenderResponseDto getAllBookingsForCalender(String laneId, LocalDate fromDate,LocalDate toDate) {
        return bookingService.getAllBookingsForCalender(laneId,fromDate,toDate);
    }

    public ResponseDto updateStatus(String bookingId, String status) {
        return bookingService.updateStatus(bookingId,status);
    }
}
