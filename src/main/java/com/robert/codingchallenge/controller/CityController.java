package com.robert.codingchallenge.controller;


import com.robert.codingchallenge.model.dto.response.GetSuggestionsResponseDTO;
import com.robert.codingchallenge.service.CityService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/city")
public class CityController {
	private final CityService cityService;

	@GetMapping("/suggestions")
	public ResponseEntity<GetSuggestionsResponseDTO> getSuggestions(
			@RequestParam
			String q,
			@Min(value = -90, message = "Latitude must be between -90 and 90")
			@Max(value = 90, message = "Latitude must be between -90 and 90")
			@RequestParam(required = false)
			Double latitude,
			@Min(value = -180, message = "Longitude must be between -180 and 180")
			@Max(value = 180, message = "Longitude must be between -180 and 180")
			@RequestParam(required = false) Double longitude
	                                                               ) {
		var cities = cityService.searchCities(q, latitude, longitude);
		GetSuggestionsResponseDTO response = new GetSuggestionsResponseDTO(cities);
		return ResponseEntity.ok(response);
	}
}
