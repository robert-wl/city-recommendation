package com.robert.codingchallenge.controller;


import com.robert.codingchallenge.model.dto.response.GetSuggestionsResponseDTO;
import com.robert.codingchallenge.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@AllArgsConstructor
@RestController
public class CityController {
	private final CityService cityService;

	@Operation(
			summary = "Get city suggestions",
			description = "Fetches city suggestions based on the provided query string. Latitude and longitude are optional parameters.",
			parameters = {
					@Parameter(name = "q", description = "The search query string for cities", required = true, example = "New York"),
					@Parameter(name = "latitude", description = "Latitude for location-based search (optional)", example = "40.7128"),
					@Parameter(name = "longitude", description = "Longitude for location-based search (optional)", example = "-74.0060")
			}
	)
	@GetMapping("/v1/suggestions")
	public ResponseEntity<GetSuggestionsResponseDTO> getSuggestions(
			@RequestParam String q,

			@Min(value = -90, message = "Latitude must be between -90 and 90")
			@Max(value = 90, message = "Latitude must be between -90 and 90")
			@RequestParam(required = false) Double latitude,

			@Min(value = -180, message = "Longitude must be between -180 and 180")
			@Max(value = 180, message = "Longitude must be between -180 and 180")
			@RequestParam(required = false) Double longitude
	                                                               ) {
		var cities = cityService.searchCities(q, latitude, longitude);
		GetSuggestionsResponseDTO response = new GetSuggestionsResponseDTO(cities);
		return ResponseEntity.ok(response);
	}
}
