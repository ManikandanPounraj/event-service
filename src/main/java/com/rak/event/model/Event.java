package com.rak.event.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "app_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Event name is required")
	private String name;

	@NotBlank(message = "Location is required")
	private String location;

	@NotNull(message = "Event date is required")
	private LocalDate date;

	@NotNull(message = "Total seats required")
	@Min(value = 1, message = "Total seats must be at least 1")
	private Integer totalSeats;

	private Integer bookedSeats = 0;

	private String description;
}
