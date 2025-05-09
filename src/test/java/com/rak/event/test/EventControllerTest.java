package com.rak.event.test;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.rak.event.controller.EventController;
import com.rak.event.exception.EventNotFoundException;
import com.rak.event.model.Event;
import com.rak.event.service.EventService;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    private EventService eventService;
    private EventController eventController;

    @BeforeEach
    void setUp() {
        eventService = mock(EventService.class);
        eventController = new EventController(eventService);
    }

    @Test
    void testCreateEvent() {
        Event input = Event.builder().name("Expo").location("Chennai").build();
        Event saved = Event.builder().id(1L).name("Expo").location("Chennai").build();

        when(eventService.createEvent(input)).thenReturn(saved);

        ResponseEntity<Event> response = eventController.create(input);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Expo", response.getBody().getName());
    }

    @Test
    void testGetEventById_Found() throws EventNotFoundException {
        Event event = Event.builder().id(1L).name("Music Fest").build();
        when(eventService.getEventDetails(1L)).thenReturn(event);

        ResponseEntity<Event> response = eventController.get(1L);
        assertEquals("Music Fest", response.getBody().getName());
    }

    @Test
    void testGetEventById_NotFound() throws EventNotFoundException {
        when(eventService.getEventDetails(99L)).thenThrow(new NoSuchElementException("Event not found"));
        assertThrows(NoSuchElementException.class, () -> eventController.get(99L));
    }

    @Test
    void testGetAllEvents() {
        List<Event> events = Arrays.asList(
            Event.builder().id(1L).name("A").build(),
            Event.builder().id(2L).name("B").build()
        );
        when(eventService.getAllEvents(null, null)).thenReturn(events);

        ResponseEntity<List<Event>> response = eventController.getAll(null, null);
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAvailability() throws EventNotFoundException {
        when(eventService.getAvailableSeats(1L)).thenReturn(50);
        ResponseEntity<Integer> response = eventController.getAvailability(1L);
        assertEquals(50, response.getBody());
    }

    @Test
    void testUpdateBookedSeats() throws EventNotFoundException {
        // No need to mock return, just verify interaction
        doNothing().when(eventService).updateBookedSeats(1L, 5);

        ResponseEntity<Void> response = eventController.updateBookedSeats(1L,
            java.util.Map.of("bookedSeats", 5));

        assertEquals(200, response.getStatusCodeValue());
        verify(eventService, times(1)).updateBookedSeats(1L, 5);
    }


    @Test
    void testDeleteEvent() throws EventNotFoundException {
        doNothing().when(eventService).deleteEvent(1L);
        ResponseEntity<Void> response = eventController.delete(1L);
        assertEquals(204, response.getStatusCodeValue());
    }
}
