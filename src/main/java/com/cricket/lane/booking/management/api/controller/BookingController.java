package com.cricket.lane.booking.management.api.controller;

import com.cricket.lane.booking.management.agent.BookingAgent;
import com.cricket.lane.booking.management.agent.LaneAgent;
import com.cricket.lane.booking.management.api.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/booking")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://sports-booking-hub.onrender.com","https://koverdrivesports.ca","https://kover-drive.onrender.com"})
public class BookingController {

    private final BookingAgent bookingAgent;

    private final LaneAgent laneAgent;

    @PostMapping
    public ResponseDto bookingCricketLane(@RequestBody CricketLaneBookingDto cricketLaneBookingDto) {
        return bookingAgent.bookingCricketLane(cricketLaneBookingDto);
    }

    @GetMapping
    public BookingPriceDto getBookingPrice(@RequestParam(value = "laneIds", required = false) List<String> laneIds,
                                           @RequestParam(value = "fromTime", required = false) LocalTime fromTime,
                                           @RequestParam(value = "toTime", required = false) LocalTime toTime,
                                           @RequestParam(value = "noOfDates",required = false) Integer noOfDates) {
        return bookingAgent.getBookingPrice(laneIds, fromTime, toTime,noOfDates);
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
                                                         @RequestParam(value = "toDate", required = false) LocalDate toDate,
                                                         @RequestHeader(value = "Authorization",required = false) String token) {
        return bookingAgent.getAllBookingsForCalender(laneId, fromDate, toDate,token);
    }

    @PutMapping
    public ResponseDto updateStatus(@RequestParam(value = "bookingId") String bookingId,
                                    @RequestParam(value = "status") String status) {
        return bookingAgent.updateStatus(bookingId, status);
    }

    @GetMapping("/lanes")
    public List<LaneDto> getAllActiveLanes(){
        return laneAgent.getAllActiveLanes();
    }

    @PostMapping("/create-lane")
    public ResponseDto createLane(@RequestBody LaneDto laneDto){
        return laneAgent.createLane(laneDto);
    }

    @DeleteMapping("/delete")
    public ResponseDto deleteBooking(@RequestParam(value = "bookingId") String bookingId){
        return bookingAgent.deleteBooking(bookingId);
    }

    @PutMapping("/change-status")
    public ResponseDto changeStatus(){
        return bookingAgent.changeStatus();
    }

    @PostMapping("/reach-us")
    public ResponseDto reachUs(@RequestBody ReachUsDto reachUsDto){
        return bookingAgent.reachUs(reachUsDto);
    }

    @GetMapping("/get-all-booking-details")
    public PaginatedResponseDto<BookingDto> getAllBookingPagination(@RequestParam(value = "fromDate") LocalDate fromDate,
                                                                    @RequestParam(value = "toDate") LocalDate toDate,
                                                                    @RequestParam(value = "laneId",required = false) String laneId,
                                                                    @RequestParam(value = "status",required = false) String status,
                                                                    @RequestParam(value = "type") String type,
                                                                    @RequestParam(value = "page") int page,
                                                                    @RequestParam(value = "size") int size){
        BookingSearchDto bookingSearchDto = BookingSearchDto.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .laneId(laneId)
                .status(status)
                .type(type)
                .page(page)
                .size(size)
                .build();

        return bookingAgent.getAllBookingPagination(bookingSearchDto);
    }

    @PutMapping("/update")
    public ResponseDto updateBooking(@RequestBody CricketLaneBookingDto cricketLaneBookingDto){
        return bookingAgent.updateBooking(cricketLaneBookingDto);
    }

    @GetMapping("/get-by-id/{id}")
    public CricketLaneBookingDto getById(@PathVariable(value = "id") String id){
        return bookingAgent.getById(id);
    }

    @PutMapping("/update-lane-status")
    public ResponseDto updateLaneStatus(@RequestParam(value = "id") String id,
                                        @RequestParam(value = "status") boolean status){
        return laneAgent.updateLaneStatus(id,status);
    }

    @GetMapping("/get-lane-by-id/{id}")
    public LaneDto getLaneById(@PathVariable(value = "id") String id){
        return laneAgent.getById(id);
    }

    @PutMapping("/update-lane")
    public ResponseDto updateLane(@RequestBody LaneDto laneDto){
        return laneAgent.updateLane(laneDto);
    }

    @GetMapping("/get-all-lanes")
    public PaginatedResponseDto<LaneDto> getAllLanes(@RequestParam(value = "page") int page,
                                                     @RequestParam(value = "size") int size){
        return laneAgent.getAllLanes(page,size);
    }

    @GetMapping("/booking-type")
    public DropDownDto getBookingType() {
        return bookingAgent.getBookingType();
    }

    @GetMapping("/booking-status")
    public DropDownDto getBookingStatus() {
        return bookingAgent.getBookingStatus();
    }

    @GetMapping("/promo-code")
    public boolean checkPromoCode(@RequestParam(value = "promoCode") String promoCode){
        return bookingAgent.checkPromoCode(promoCode);
    }
}
