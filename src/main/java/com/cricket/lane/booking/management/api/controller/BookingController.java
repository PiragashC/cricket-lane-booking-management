package com.cricket.lane.booking.management.api.controller;

import com.cricket.lane.booking.management.agent.BookingAgent;
import com.cricket.lane.booking.management.api.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/booking")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

    private final BookingAgent bookingAgent;

    @PostMapping
    public ResponseDto bookingCricketLane(@RequestBody CricketLaneBookingDto cricketLaneBookingDto) {
        return bookingAgent.bookingCricketLane(cricketLaneBookingDto);
    }

    @GetMapping
    public BookingPriceDto getBookingPrice(@RequestParam(value = "noOfLanes", required = false) Integer noOfLanes,
                                           @RequestParam(value = "fromTime", required = false) LocalTime fromTime,
                                           @RequestParam(value = "toTime", required = false) LocalTime toTime) {
        return bookingAgent.getBookingPrice(noOfLanes, fromTime, toTime);
    }

    @GetMapping("/check-availability")
    public List<LaneDto> checkLaneAvailability(@RequestParam(value = "fromTime", required = false) LocalTime fromTime,
                                               @RequestParam(value = "toTime", required = false) LocalTime toTime,
                                               @RequestParam(value = "date", required = false) List<LocalDate> date) {
        return bookingAgent.checkLaneAvailability(fromTime, toTime, date);
    }

    @GetMapping("/get-all-for-calender")
    public CalenderResponseDto getAllBookingsForCalender(@RequestParam(value = "laneId", required = false) String laneId,
                                                         @RequestParam(value = "fromDate", required = false) LocalDate fromDate,
                                                         @RequestParam(value = "toDate", required = false) LocalDate toDate) {
        return bookingAgent.getAllBookingsForCalender(laneId, fromDate, toDate);
    }

    @PutMapping
    public ResponseDto updateStatus(@RequestParam(value = "bookingId") String bookingId,
                                    @RequestParam(value = "status") String status) {
        return bookingAgent.updateStatus(bookingId, status);
    }
}
