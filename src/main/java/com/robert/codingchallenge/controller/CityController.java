package com.robert.codingchallenge.controller;


import com.robert.codingchallenge.model.City;
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
	public ResponseEntity<List<City>> getSuggestions() {
		List<City> cities = cityService.getAllCities().stream().limit(100).collect(Collectors.toList());
		return ResponseEntity.ok(cities);
	}
}
