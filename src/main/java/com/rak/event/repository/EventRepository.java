package com.rak.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rak.event.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
	 List<Event> findByLocationAndDate(String location, LocalDate date);
}
