package com.rak.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.rak.event.exception.EventNotFoundException;
import com.rak.event.model.Event;
import com.rak.event.repository.EventRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

	private final EventRepository repo;

	public Event create(Event event) {
		log.info("Creating new event: {}", event.getName());
		return repo.save(event);
	}

	public Event get(Long id) {
		log.debug("Fetching event with ID: {}", id);
		return repo.findById(id).orElseThrow(() -> {
			log.error("Event not found with ID: {}", id);
			return new EventNotFoundException("Event not found with id: " + id);
		});
	}

	public List<Event> getAll(String location, LocalDate date) {
		if (location != null && date != null) {
			log.info("Searching events in location '{}' on '{}'", location, date);
			return repo.findByLocationAndDate(location, date);
		}
		log.debug("Fetching all events");
		return repo.findAll();
	}

	public Event update(Long id, Event updated) {
		log.info("Updating event ID: {}", id);
		Event event = get(id);
		event.setName(updated.getName());
		event.setLocation(updated.getLocation());
		event.setDate(updated.getDate());
		event.setDescription(updated.getDescription());
		event.setTotalSeats(updated.getTotalSeats());
		return repo.save(event);
	}

	public void delete(Long id) {
		log.warn("Deleting event ID: {}", id);
		Event event = get(id);
		repo.delete(event);
	}

	public int getAvailableSeats(Long id) {
		Event event = get(id);
		int available = event.getTotalSeats() - event.getBookedSeats();
		log.info("Available seats for event ID {}: {}", id, available);
		return available;
	}

	public void updateBookedSeats(Long id, int newBookedSeats) {
		log.info("Updating booked seats for event ID {} to {}", id, newBookedSeats);
		Event event = get(id);
		event.setBookedSeats(newBookedSeats);
		repo.save(event);
	}
}
