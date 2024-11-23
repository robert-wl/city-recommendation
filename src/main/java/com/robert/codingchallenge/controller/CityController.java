package com.robert.codingchallenge.controller;


import com.robert.codingchallenge.model.dto.response.GetSuggestionsResponseDTO;
import com.robert.codingchallenge.service.CityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController("/api/city")
public class CityController {
	private final CityService cityService;

	@GetMapping("/suggestions")
	public ResponseEntity<GetSuggestionsResponseDTO> getSuggestions(
			@RequestParam String q,
			@RequestParam(required = false) Double latitude,
			@RequestParam(required = false) Double longitude
	                                                               ) {
		var cities = cityService.searchCities(q, latitude, longitude);
		GetSuggestionsResponseDTO response = new GetSuggestionsResponseDTO(cities);
		return ResponseEntity.ok(response);
	}
}
