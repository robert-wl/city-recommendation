package com.robert.codingchallenge.controller;


import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.model.dto.GetSuggestionsResponseDTO;
import com.robert.codingchallenge.service.CityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController("/api/city")
public class CityController {
	private final CityService cityService;

	@GetMapping("/suggestions")
	public ResponseEntity<GetSuggestionsResponseDTO> getSuggestions() {
		List<City> cities = cityService.getAllCities().stream().limit(100).collect(Collectors.toList());
		GetSuggestionsResponseDTO response = new GetSuggestionsResponseDTO(cities);
		return ResponseEntity.ok(response);
	}
}
