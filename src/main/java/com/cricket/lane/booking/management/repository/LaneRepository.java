package com.cricket.lane.booking.management.repository;

import com.cricket.lane.booking.management.api.dto.LaneDto;
import com.cricket.lane.booking.management.entity.Lanes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface LaneRepository extends JpaRepository<Lanes,String> {

    @Query("SELECT l.laneName " +
            "FROM Lanes l " +
            "WHERE l.id = :laneId")
    String findLaneNameById(String laneId);

    @Query("SELECT NEW com.cricket.lane.booking.management.api.dto.LaneDto(l.id,l.laneName) " +
            "FROM Lanes l " +
            "WHERE l.isActive = true")
    List<LaneDto> getAllActiveLanes();

    @Query("SELECT NEW com.cricket.lane.booking.management.api.dto.LaneDto(l.id,l.laneName) " +
            "FROM Lanes l " +
            "WHERE l.id = :id")
    LaneDto getLaneById(String id);

    @Query("SELECT NEW com.cricket.lane.booking.management.api.dto.LaneDto(l.id,l.laneName,l.isActive) " +
            "FROM Lanes l ")
    Page<LaneDto> geAllLanes(Pageable pageable);

    @Query("SELECT l.lanePrice " +
            "FROM Lanes l " +
            "WHERE l.id = :laneId")
    BigDecimal findLanePrice(String laneId);
}
