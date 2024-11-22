package com.robert.codingchallenge.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/city")
public class CityController {

	@GetMapping("/suggestions")
	public ResponseEntity<String> getSuggestions() {
		String response = "Hello World!";
		return ResponseEntity.ok(response);
	}
}
