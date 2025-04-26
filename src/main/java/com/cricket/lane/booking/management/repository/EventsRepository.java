package com.cricket.lane.booking.management.repository;

import com.cricket.lane.booking.management.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventsRepository extends JpaRepository<Event,String> {
}
