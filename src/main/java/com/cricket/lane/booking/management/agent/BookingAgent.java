package com.cricket.lane.booking.management.agent;

import com.cricket.lane.booking.management.agent.converter.BookingConverter;
import com.cricket.lane.booking.management.api.dto.*;
import com.cricket.lane.booking.management.enums.BookingStatus;
import com.cricket.lane.booking.management.enums.BookingType;
import com.cricket.lane.booking.management.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    public BookingPriceDto getBookingPrice(List<String> laneIds, LocalTime fromTime, LocalTime toTime,Integer noOfDates) {
        return bookingService.getBookingPrice(laneIds,fromTime,toTime,noOfDates);
    }

    public List<LaneDto> checkLaneAvailability(LocalTime fromTime, LocalTime toTime, List<LocalDate> date) {
        return bookingService.checkLaneAvailability(fromTime,toTime,date);
    }

    public CalenderResponseDto getAllBookingsForCalender(String laneId, LocalDate fromDate,LocalDate toDate,String token) {
        return bookingService.getAllBookingsForCalender(laneId,fromDate,toDate,token);
    }

    public ResponseDto updateStatus(String bookingId, String status) {
        return bookingService.updateStatus(bookingId,status);
    }

    public ResponseDto deleteBooking(String bookingId) {
        return bookingService.deleteBooking(bookingId);
    }

    public ResponseDto changeStatus() {
        return bookingService.changeStatus();
    }

    public ResponseDto reachUs(ReachUsDto reachUsDto) {
        return bookingService.reachUs(reachUsDto);
    }

    public PaginatedResponseDto<BookingDto> getAllBookingPagination(BookingSearchDto bookingSearchDto){
        Page<BookingDto> bookingDtos = bookingService.getAllBookingPagination(bookingSearchDto);
        List<BookingDto> bookingDtoList = bookingDtos.getContent();

        PaginatedResponseDto<BookingDto> bookingDtoPaginatedResponseDto = new PaginatedResponseDto<>();
        bookingDtoPaginatedResponseDto.setData(bookingDtoList);
        bookingDtoPaginatedResponseDto.setTotalItems(bookingDtos.getTotalElements());
        bookingDtoPaginatedResponseDto.setCurrentPage(bookingSearchDto.getPage());
        bookingDtoPaginatedResponseDto.setTotalPages(bookingDtos.getTotalPages());
        return bookingDtoPaginatedResponseDto;
    }

    public ResponseDto updateBooking(CricketLaneBookingDto cricketLaneBookingDto) {
        return bookingService.updateBooking(cricketLaneBookingDto);
    }

    public CricketLaneBookingDto getById(String id) {
        return bookingConverter.convert(bookingService.getById(id));
    }

    public DropDownDto getBookingType() {
        DropDownDto dto = new DropDownDto();
        dto.setBookingType(BookingType.getAll());
        return dto;
    }

    public DropDownDto getBookingStatus() {
        DropDownDto dto = new DropDownDto();
        dto.setBookingStatus(BookingStatus.getAll());
        return dto;
    }

    public boolean checkPromoCode(String promoCode) {
        return bookingService.checkPromoCode(promoCode);
    }
}
