package com.cricket.lane.booking.management.repository;

import com.cricket.lane.booking.management.api.dto.BookingDto;
import com.cricket.lane.booking.management.api.dto.BookingResponseDto;
import com.cricket.lane.booking.management.api.dto.LaneDto;
import com.cricket.lane.booking.management.entity.CricketLaneBooking;
import com.cricket.lane.booking.management.entity.Lanes;
import com.cricket.lane.booking.management.enums.BookingStatus;
import com.cricket.lane.booking.management.enums.BookingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public interface CricketLaneBookingRepository extends JpaRepository<CricketLaneBooking,String> {

    @Query("SELECT DISTINCT NEW com.cricket.lane.booking.management.api.dto.LaneDto(l.id, l.laneName) " +
            "FROM Lanes l " +
            "WHERE l.id NOT IN ( " +
            "    SELECT DISTINCT sl.laneId " +
            "    FROM SelectedLanes sl " +
            "    INNER JOIN CricketLaneBooking cb ON sl.cricketLaneBookingId = cb.id " +
            "    INNER JOIN BookingDates bd ON cb.id = bd.cricketLaneBookingId " +
            "    WHERE bd.bookingDate IN :dates " +
            "    AND cb.fromTime <= :toTime " +
            "    AND cb.toTime >= :fromTime " +
            "    AND cb.bookingStatus IN ('SUCCESS', 'PENDING') " +
            ")")
    List<LaneDto> findAvailableLanes(
            @Param("fromTime") LocalTime fromTime,
            @Param("toTime") LocalTime toTime,
            @Param("dates") List<LocalDate> dates
    );


    @Query("SELECT NEW com.cricket.lane.booking.management.api.dto.BookingResponseDto(c.id, CONCAT(c.firstName,' ', c.lastName) AS userName,c.fromTime,c.toTime,c.telephoneNumber,c.bookingPrice,c.bookingStatus,c.bookingTitle) " +
            "FROM CricketLaneBooking c " +
            "LEFT JOIN BookingDates b ON c.id = b.cricketLaneBookingId " +
            "LEFT JOIN SelectedLanes s ON c.id = s.cricketLaneBookingId " +
            "WHERE s.laneId = :laneId " +
            "AND b.bookingDate BETWEEN :fromDate AND :toDate " +
            "AND (c.bookingStatus = 'SUCCESS' OR c.bookingStatus = 'PENDING')")
    List<BookingResponseDto> getAllBookingsForCalenderAdmin(String laneId, LocalDate fromDate,LocalDate toDate);

    @Query("SELECT NEW com.cricket.lane.booking.management.api.dto.BookingResponseDto(c.id,c.fromTime,c.toTime) " +
            "FROM CricketLaneBooking c " +
            "LEFT JOIN BookingDates b ON c.id = b.cricketLaneBookingId " +
            "LEFT JOIN SelectedLanes s ON c.id = s.cricketLaneBookingId " +
            "WHERE s.laneId = :laneId " +
            "AND b.bookingDate BETWEEN :fromDate AND :toDate " +
            "AND (c.bookingStatus = 'SUCCESS' OR c.bookingStatus = 'PENDING')")
    List<BookingResponseDto> getAllBookingsForCalenderUser(String laneId, LocalDate fromDate,LocalDate toDate);

    @Query("SELECT COUNT(c) > 0 " +
            "FROM CricketLaneBooking c " +
            "JOIN BookingDates b ON c.id = b.cricketLaneBookingId " +
            "JOIN SelectedLanes s ON c.id = s.cricketLaneBookingId " +
            "WHERE b.bookingDate = :date " +
            "AND (:fromTime < c.toTime AND :toTime > c.fromTime) " +
            "AND (c.bookingStatus = 'SUCCESS' OR c.bookingStatus = 'PENDING') " +
            "AND s.laneId = :laneId")
    Boolean checkLaneFree(@Param("fromTime") LocalTime fromTime,
                          @Param("toTime") LocalTime toTime,
                          @Param("date") LocalDate date,@Param("laneId") String laneId);

    @Query("SELECT c " +
            "FROM CricketLaneBooking c " +
            "WHERE c.fromTime < :time " +
            "AND c.createdDate = :date " +
            "AND c.bookingStatus = 'PENDING'")
    List<CricketLaneBooking> stillPending(LocalDate date,LocalTime time);

    @Query("SELECT NEW com.cricket.lane.booking.management.api.dto.BookingDto(c.id,CONCAT(c.firstName,' ',c.lastName) AS customerName,b.bookingDate,c.fromTime, c.toTime,l.laneName,c.bookingStatus,c.email,c.telephoneNumber) " +
            "FROM CricketLaneBooking c " +
            "LEFT JOIN BookingDates b ON c.id = b.cricketLaneBookingId " +
            "LEFT JOIN SelectedLanes s ON c.id = s.cricketLaneBookingId " +
            "LEFT JOIN Lanes l ON s.laneId = l.id " +
            "WHERE b.bookingDate BETWEEN :fromDate AND :toDate " +
            "AND (:laneId IS NULL OR s.laneId = :laneId) " +
            "AND (:status IS NULL OR c.bookingStatus = :status) " +
            "AND (:type IS NULL OR c.bookingType = :type) " +
            "ORDER BY b.bookingDate DESC")
    Page<BookingDto> getAllBookingPagination(Pageable pageable, LocalDate fromDate, LocalDate toDate, String laneId,
                                             BookingStatus status, BookingType type);

}
