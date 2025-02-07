package com.cricket.lane.booking.management.repository;

import com.cricket.lane.booking.management.api.dto.BookingResponseDto;
import com.cricket.lane.booking.management.api.dto.LaneDto;
import com.cricket.lane.booking.management.entity.CricketLaneBooking;
import com.cricket.lane.booking.management.entity.Lanes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public interface CricketLaneBookingRepository extends JpaRepository<CricketLaneBooking,String> {

    @Query("SELECT DISTINCT NEW com.cricket.lane.booking.management.api.dto.LaneDto(l.id, l.laneName) " +
            "FROM Lanes l " +
            "LEFT JOIN SelectedLanes s ON l.id = s.laneId " +
            "LEFT JOIN CricketLaneBooking c ON s.cricketLaneBookingId = c.id " +
            "LEFT JOIN BookingDates b ON c.id = b.cricketLaneBookingId " +
            "WHERE (b.bookingDate NOT IN :dates OR b.bookingDate IS NULL) " +
            "AND :fromTime < c.toTime " +
            "AND :toTime > c.fromTime " +
            "GROUP BY l.id " +
            "HAVING COUNT(DISTINCT b.bookingDate) = :totalDates")
    List<LaneDto> findAvailableLanes(
            @Param("fromTime") LocalTime fromTime,
            @Param("toTime") LocalTime toTime,
            @Param("dates") List<LocalDate> dates,
            @Param("totalDates") int totalDates
    );


    @Query("SELECT NEW com.cricket.lane.booking.management.api.dto.BookingResponseDto(c.id, CONCAT(c.firstName,' ', c.lastName) AS userName,c.fromTime,c.toTime) " +
            "FROM CricketLaneBooking c " +
            "LEFT JOIN BookingDates b ON c.id = b.cricketLaneBookingId " +
            "LEFT JOIN SelectedLanes s ON c.id = s.cricketLaneBookingId " +
            "WHERE s.laneId = :laneId " +
            "AND b.bookingDate BETWEEN :fromDate AND :toDate " +
            "AND (c.bookingStatus = 'SUCCESS' OR c.bookingStatus = 'PENDING')")
    List<BookingResponseDto> getAllBookingsForCalender(String laneId, LocalDate fromDate,LocalDate toDate);

    @Query("SELECT COUNT(c) > 0 " +
            "FROM CricketLaneBooking c " +
            "JOIN BookingDates b ON c.id = b.cricketLaneBookingId " +
            "JOIN SelectedLanes s ON c.id = s.cricketLaneBookingId " +
            "WHERE b.bookingDate = :date " +
            "AND (:fromTime < c.toTime AND :toTime > c.fromTime)")
    Boolean checkLaneFree(@Param("fromTime") LocalTime fromTime,
                          @Param("toTime") LocalTime toTime,
                          @Param("date") LocalDate date);

}
