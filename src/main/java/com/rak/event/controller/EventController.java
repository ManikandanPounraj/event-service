package com.rak.event.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rak.event.exception.EventNotFoundException;
import com.rak.event.model.Event;
import com.rak.event.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

	private final EventService service;

	@PostMapping
	@Operation(summary = "Create new event")
	public ResponseEntity<Event> create(@Validated @RequestBody Event event) {
		log.info("Received request to create event: {}", event.getName());
		return ResponseEntity.status(201).body(service.createEvent(event));
	}

	@GetMapping
	@Operation(summary = "List or search events")
	public ResponseEntity<List<Event>> getAll(@RequestParam(required = false) String location,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		log.debug("Requesting event list with location={}, date={}", location, date);
		return ResponseEntity.ok(service.getAllEvents(location, date));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get event by ID")
	public ResponseEntity<Event> get(@PathVariable Long id) throws EventNotFoundException {
		log.debug("Fetching event ID: {}", id);
		return ResponseEntity.ok(service.getEventDetails(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update an event")
	public ResponseEntity<Event> update(@PathVariable Long id, @Validated @RequestBody Event updated) throws EventNotFoundException {
		log.info("Updating event ID: {}", id);
		return ResponseEntity.ok(service.updateEvent(id, updated));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete event by ID")
	public ResponseEntity<Void> delete(@PathVariable Long id) throws EventNotFoundException {
		log.warn("Received request to delete event ID: {}", id);
		service.deleteEvent(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/availability")
	@Operation(summary = "Check available seats for an event")
	public ResponseEntity<Integer> getAvailability(@PathVariable Long id) throws EventNotFoundException {
		log.debug("Checking seat availability for event ID: {}", id);
		return ResponseEntity.ok(service.getAvailableSeats(id));
	}

	@PutMapping("/{id}/bookedSeats")
	@Operation(summary = "Update booked seat count")
	public ResponseEntity<Void> updateBookedSeats(@PathVariable Long id, @RequestBody Map<String, Integer> req) throws EventNotFoundException {
		log.info("Updating booked seats for event ID {}: {}", id, req.get("bookedSeats"));
		service.updateBookedSeats(id, req.get("bookedSeats"));
		return ResponseEntity.ok().build();
	}
}
