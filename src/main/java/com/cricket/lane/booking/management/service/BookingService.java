package com.cricket.lane.booking.management.service;

import com.cricket.lane.booking.management.agent.converter.BookingConverter;
import com.cricket.lane.booking.management.api.dto.*;
import com.cricket.lane.booking.management.entity.BookingDates;
import com.cricket.lane.booking.management.entity.CricketLaneBooking;
import com.cricket.lane.booking.management.entity.Lanes;
import com.cricket.lane.booking.management.entity.PromoCode;
import com.cricket.lane.booking.management.enums.BookingStatus;
import com.cricket.lane.booking.management.enums.BookingType;
import com.cricket.lane.booking.management.exception.ServiceException;
import com.cricket.lane.booking.management.repository.CricketLaneBookingRepository;
import com.cricket.lane.booking.management.repository.LaneRepository;
import com.cricket.lane.booking.management.repository.PromoCodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cricket.lane.booking.management.constants.ApplicationConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final CricketLaneBookingRepository cricketLaneBookingRepository;

    private final LaneRepository laneRepository;

    private final EmailNotificationService emailNotificationService;

    private static final SecureRandom random = new SecureRandom();

    private static final Set<Integer> generatedNumbers = new HashSet<>();

    private final BookingConverter bookingConverter;

    @Value("${admin.email}")
    private String adminEmail;

    private final PromoCodeRepository promoCodeRepository;


    public ResponseDto bookingCricketLane(CricketLaneBooking cricketLaneBooking) {
        cricketLaneBooking.getBookingDates().stream()
                .map(bookingDate -> {

                    cricketLaneBooking.getSelectedLanes().stream()
                            .map(selectedLane -> {
                                Boolean checkLaneFree = cricketLaneBookingRepository.checkLaneFree(cricketLaneBooking.getFromTime(), cricketLaneBooking.getToTime(), bookingDate.getBookingDate(), selectedLane.getLaneId());
                                log.info("checkLane---{}", checkLaneFree);
                                if (checkLaneFree.equals(Boolean.TRUE)) {
                                    throw new ServiceException("Lane already booked on " + bookingDate.getBookingDate() +
                                            " from " + cricketLaneBooking.getFromTime() + " to " + cricketLaneBooking.getToTime(), BAD_REQUEST, HttpStatus.BAD_REQUEST);
                                }
                                laneRepository.findById(selectedLane.getLaneId()).orElseThrow(() -> new ServiceException(LANE_NOT_FOUND, BAD_REQUEST, HttpStatus.BAD_REQUEST));
                                return selectedLane;
                            }).collect(Collectors.toSet());
                    return bookingDate;
                }).collect(Collectors.toSet());
        CricketLaneBooking savedBooking = cricketLaneBookingRepository.save(cricketLaneBooking);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(BOOKING_SAVED);
        responseDto.setBookingId(savedBooking.getId());
        return responseDto;
    }

    public BookingPriceDto getBookingPrice(List<String> laneIds, LocalTime fromTime, LocalTime toTime, Integer noOfDates, String promoCode) {
        BookingPriceDto bookingPriceDto = new BookingPriceDto();
        long durationMinutes = Duration.between(fromTime, toTime).toMinutes();
        long durationHours = (long) Math.ceil(durationMinutes / 60.0);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (String laneId : laneIds) {
            BigDecimal lanePrice = laneRepository.findLanePrice(laneId);
            BigDecimal laneTotal = lanePrice.multiply(BigDecimal.valueOf(durationHours * noOfDates));
            totalPrice = totalPrice.add(laneTotal);
        }

        BigDecimal totalPriceWithTax = totalPrice.multiply(BigDecimal.valueOf(1.13));

        PromoCode koverDrivePromoCode = promoCodeRepository.getPromoCodeToCalculatePrice();

        if (koverDrivePromoCode != null && koverDrivePromoCode.getPromoCode().equals(promoCode)) {
            BigDecimal discountPercentage = koverDrivePromoCode.getDiscount();
            BigDecimal discountAmount = totalPriceWithTax.multiply(discountPercentage.divide(BigDecimal.valueOf(100)));
            totalPriceWithTax = totalPriceWithTax.subtract(discountAmount);
        }

        bookingPriceDto.setBookingPrice(totalPriceWithTax);
        return bookingPriceDto;
    }



    public List<LaneDto> checkLaneAvailability(LocalTime fromTime, LocalTime toTime, List<LocalDate> dates) {
        return cricketLaneBookingRepository.findAvailableLanes(fromTime, toTime, dates);
    }

    public CalenderResponseDto getAllBookingsForCalender(String laneId, LocalDate fromDate, LocalDate toDate, String token) {
        CalenderResponseDto calenderResponseDto = new CalenderResponseDto();
        String laneName = laneRepository.findLaneNameById(laneId);
        List<WeekMonthViewResponseDto> weekMonthViewResponseDtos = new ArrayList<>();

        calenderResponseDto.setLaneId(laneId);

        if (fromDate.equals(toDate)) {
            if (token == null) {
                List<BookingResponseDto> bookingResponseDtos = cricketLaneBookingRepository.getAllBookingsForCalenderUser(laneId, fromDate, toDate);
                calenderResponseDto.setBookingDate(fromDate);
                calenderResponseDto.setLaneName(laneName);
                calenderResponseDto.setBookingResponseDtos(bookingResponseDtos);
            } else {
                List<BookingResponseDto> bookingResponseDtos = cricketLaneBookingRepository.getAllBookingsForCalenderAdmin(laneId, fromDate, toDate);
                calenderResponseDto.setBookingDate(fromDate);
                calenderResponseDto.setLaneName(laneName);
                calenderResponseDto.setBookingResponseDtos(bookingResponseDtos);
            }
        } else {
            List<LocalDate> dateList = getDatesBetween(fromDate, toDate);

            dateList.forEach(date -> {
                if (token == null) {
                    List<BookingResponseDto> bookingResponseDtosForDate = cricketLaneBookingRepository.getAllBookingsForCalenderUser(laneId, date, date);
                    WeekMonthViewResponseDto weekMonthViewResponseDto = new WeekMonthViewResponseDto();
                    weekMonthViewResponseDto.setBookingDate(date);
                    weekMonthViewResponseDto.setBookingResponseDtos(bookingResponseDtosForDate);
                    weekMonthViewResponseDtos.add(weekMonthViewResponseDto);
                } else {
                    List<BookingResponseDto> bookingResponseDtosForDate = cricketLaneBookingRepository.getAllBookingsForCalenderAdmin(laneId, date, date);
                    WeekMonthViewResponseDto weekMonthViewResponseDto = new WeekMonthViewResponseDto();
                    weekMonthViewResponseDto.setBookingDate(date);
                    weekMonthViewResponseDto.setBookingResponseDtos(bookingResponseDtosForDate);
                    weekMonthViewResponseDtos.add(weekMonthViewResponseDto);
                }
            });

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

        if (status.equalsIgnoreCase(BookingStatus.SUCCESS.getMappedValue())) {
            sendConfirmationEmail(cricketLaneBooking);
        }

        return new ResponseDto(BOOKING_STATUS_UPDATED);
    }

    private void sendConfirmationEmail(CricketLaneBooking cricketLaneBooking) {
        EmailDataDto emailDataDto = new EmailDataDto();
        emailDataDto.setSubject("Indoor Cricket Lane Booking Confirmation – Kover Drive");
        emailDataDto.setServiceProvider("kover_drive");
        emailDataDto.setMailTemplateName("booking_confirmation");

        emailDataDto.setRecipients(Collections.singletonList(cricketLaneBooking.getEmail()));

        Map<String, Object> data = new HashMap<>();
        data.put("name", cricketLaneBooking.getFirstName());

        String lanes = cricketLaneBooking.getSelectedLanes().stream()
                .map(lane -> laneRepository.findLaneNameById(lane.getLaneId()))
                .collect(Collectors.joining(", "));

        String dates = cricketLaneBooking.getBookingDates().stream()
                .map(BookingDates::getBookingDate)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        Duration duration = Duration.between(cricketLaneBooking.getFromTime(), cricketLaneBooking.getToTime());

        data.put("laneNumber", lanes);
        data.put("date", dates);
        data.put("time", cricketLaneBooking.getFromTime());
        data.put("duration", duration.toHours() + " hours");
        data.put("bookingReference", generateUniqueReference());

        emailDataDto.setData(data);

        try {
            emailNotificationService.send(emailDataDto);
        } catch (Exception e) {
            throw new ServiceException("EMAIL_SENDING_FAILED", BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }
    }

    private String generateUniqueReference() {
        int number;
        do {
            number = 100000 + random.nextInt(900000);
        } while (!generatedNumbers.add(number));

        return "REF-" + number;
    }

    public ResponseDto deleteBooking(String bookingId) {
        cricketLaneBookingRepository.findById(bookingId)
                .orElseThrow(() -> new ServiceException(BOOKING_ID_NOT_FOUND, BAD_REQUEST, HttpStatus.BAD_REQUEST));
        cricketLaneBookingRepository.deleteById(bookingId);

        return new ResponseDto("Booking deleted");
    }

    public ResponseDto changeStatus() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTimeMinus30 = LocalTime.now().minusMinutes(30);

        List<CricketLaneBooking> cricketLaneBookings = cricketLaneBookingRepository.stillPending(currentDate, currentTimeMinus30);
        cricketLaneBookings.stream()
                .map(cricketLaneBooking -> {
                    cricketLaneBooking.setBookingStatus(BookingStatus.FAILURE);
                    cricketLaneBookingRepository.save(cricketLaneBooking);
                    return cricketLaneBookings;
                }).toList();

        return new ResponseDto("Status updated");
    }

    public ResponseDto reachUs(ReachUsDto reachUsDto) {
        EmailDataDto emailDataDto = new EmailDataDto();
        emailDataDto.setRecipients(List.of(adminEmail));
        emailDataDto.setServiceProvider("kover_drive");
        emailDataDto.setSubject("New Contact Inquiry");
        emailDataDto.setMailTemplateName("reach_us");
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("name", reachUsDto.getName());
        emailData.put("email", reachUsDto.getEmail());
        emailData.put("subject", reachUsDto.getSubject());
        emailData.put("message", reachUsDto.getMessage());

        emailDataDto.setData(emailData);
        emailNotificationService.send(emailDataDto);

        return new ResponseDto("Mail sent to admin");
    }

    public Page<BookingDto> getAllBookingPagination(BookingSearchDto bookingSearchDto) {
        Pageable pageable = PageRequest.of(bookingSearchDto.getPage() - 1, bookingSearchDto.getSize());
        Page<BookingDto> bookingDtos = null;
        if (bookingSearchDto.getType().equalsIgnoreCase("All")){
            bookingDtos = cricketLaneBookingRepository.getAllBookingPaginationAllType(pageable,
                    bookingSearchDto.getFromDate(),
                    bookingSearchDto.getToDate(),
                    bookingSearchDto.getLaneId(),
                    bookingSearchDto.getStatus() != null && !bookingSearchDto.getStatus().isEmpty() ? BookingStatus.fromMappedValue(bookingSearchDto.getStatus()) : null);


        }else {
            bookingDtos = cricketLaneBookingRepository.getAllBookingPagination(
                    pageable,
                    bookingSearchDto.getFromDate(),
                    bookingSearchDto.getToDate(),
                    bookingSearchDto.getLaneId(),
                    bookingSearchDto.getStatus() != null && !bookingSearchDto.getStatus().isEmpty() ? BookingStatus.fromMappedValue(bookingSearchDto.getStatus()) : null,
                    bookingSearchDto.getType() != null && !bookingSearchDto.getType().isEmpty() ? BookingType.fromMappedValue(bookingSearchDto.getType()) : null
            );
        }
        BookingDto.resetCounter();

        List<BookingDto> updatedList = bookingDtos.getContent().stream()
                .map(dto -> new BookingDto(dto.getId(), dto.getCustomerName(), dto.getDate(), dto.getFromTime(),
                        dto.getToTime(), dto.getLaneName(), BookingStatus.fromMappedValue(dto.getStatus()), dto.getEmail(), dto.getTelephoneNumber()))
                .toList();

        return new PageImpl<>(updatedList, pageable, bookingDtos.getTotalElements());
    }

    public ResponseDto updateBooking(CricketLaneBookingDto cricketLaneBookingDto) {
        CricketLaneBooking existingBooking = cricketLaneBookingRepository.findById(cricketLaneBookingDto.getId())
                .orElseThrow(() -> new ServiceException(BOOKING_ID_NOT_FOUND, BAD_REQUEST, HttpStatus.BAD_REQUEST));

        cricketLaneBookingRepository.save(bookingConverter.convertForUpdate(existingBooking, cricketLaneBookingDto));
        return new ResponseDto("Booking updated successfully");
    }

    public CricketLaneBooking getById(String id) {
        CricketLaneBooking existingBooking = cricketLaneBookingRepository.findById(id)
                .orElseThrow(() -> new ServiceException(BOOKING_ID_NOT_FOUND, BAD_REQUEST, HttpStatus.BAD_REQUEST));

        return existingBooking;
    }

    public boolean checkPromoCode(String promoCode) {
        String koverDrivePromo = promoCodeRepository.getPromoCode();
        if (koverDrivePromo != null && koverDrivePromo.equals(promoCode)) {
            return true;
        } else {
            return false;
        }
    }

    public PromoCodeDto getPromoCode() {
        PromoCode promoCode = promoCodeRepository.getPromoCodeToCalculatePrice();
        PromoCodeDto promoCodeDto = new PromoCodeDto();
        promoCodeDto.setId(promoCode.getId());
        promoCodeDto.setPromoCode(promoCode.getPromoCode());
        promoCodeDto.setDiscount(promoCode.getDiscount());
        promoCodeDto.setIsActive(promoCode.getIsActive());
        return promoCodeDto;
    }

    public ResponseDto updatePromoCode(PromoCodeDto promoCodeDto) {
        PromoCode promoCode = promoCodeRepository.findById(promoCodeDto.getId()).orElseThrow(() -> new ServiceException("Promo code not found",BAD_REQUEST,HttpStatus.BAD_REQUEST));
        promoCode.setId(promoCodeDto.getId());
        promoCode.setPromoCode(promoCodeDto.getPromoCode());
        promoCode.setDiscount(promoCodeDto.getDiscount());
        promoCodeRepository.save(promoCode);

        return new ResponseDto("Promo code updated successfully");
    }

    public ResponseDto updatePromoCodeStatus(String id, Boolean status) {
        PromoCode promoCode = promoCodeRepository.findById(id).orElseThrow(() -> new ServiceException("Promo code not found",BAD_REQUEST,HttpStatus.BAD_REQUEST));
        promoCode.setIsActive(status);
        promoCodeRepository.save(promoCode);

        return new ResponseDto("Promo code status updated successfully");
    }
}
