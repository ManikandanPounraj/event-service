package com.rak.event.test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rak.event.exception.EventNotFoundException;
import com.rak.event.model.Event;
import com.rak.event.repository.EventRepository;
import com.rak.event.service.EventService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    private EventRepository repository;
    private EventService service;

    @BeforeEach
    void setUp() {
        repository = mock(EventRepository.class);
        service = new EventService(repository);
    }

    @Test
    void testCreateEvent() {
        Event input = Event.builder().name("Conference").location("Madurai").totalSeats(100).build();
        Event saved = Event.builder().id(1L).name("Conference").location("Madurai").totalSeats(100).build();

        when(repository.save(input)).thenReturn(saved);

        Event result = service.createEvent(input);
        assertEquals("Conference", result.getName());
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetEventById_Found() throws EventNotFoundException {
        Event event = Event.builder().id(1L).name("Expo").build();
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        Event result = service.getEventDetails(1L);
        assertEquals("Expo", result.getName());
    }

    @Test
    void testGetEventById_NotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> service.getEventDetails(99L));
    }

    @Test
    void testGetAvailableSeats() throws EventNotFoundException {
        Event event = Event.builder().id(1L).totalSeats(100).bookedSeats(40).build();
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        int available = service.getAvailableSeats(1L);
        assertEquals(60, available);
    }

    @Test
    void testUpdateBookedSeats() throws EventNotFoundException {
        Event event = Event.builder().id(1L).bookedSeats(10).build();
        when(repository.findById(1L)).thenReturn(Optional.of(event));
        when(repository.save(any())).thenReturn(event);

        service.updateBookedSeats(1L, 5);

        // Validate state was updated
        assertEquals(5, event.getBookedSeats());

        // Validate save was called
        verify(repository).save(event);
    }


//    @Test
//    void testDeleteEvent() {
//        doNothing().when(repository).deleteById(1L);
//        service.delete(1L);
//        verify(repository).deleteById(1L);
//    }
}
