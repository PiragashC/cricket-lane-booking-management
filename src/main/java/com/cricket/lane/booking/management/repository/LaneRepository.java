package com.cricket.lane.booking.management.repository;

import com.cricket.lane.booking.management.api.dto.LaneDto;
import com.cricket.lane.booking.management.entity.Lanes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
