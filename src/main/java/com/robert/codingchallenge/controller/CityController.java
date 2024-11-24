package com.robert.codingchallenge.controller;


import com.robert.codingchallenge.model.dto.request.SuggestionsRequestDTO;
import com.robert.codingchallenge.model.dto.response.SuggestionsResponseDTO;
import com.robert.codingchallenge.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@Validated
@AllArgsConstructor
@RestController
public class CityController {
	private final CityService cityService;

	@Operation(
			summary = "Get city suggestions",
			description = "Fetches city suggestions based on the provided query string. Latitude and longitude are optional parameters."
	)
	@GetMapping("/v1/suggestions")
	public ResponseEntity<SuggestionsResponseDTO> getSuggestions(
			@ParameterObject @ModelAttribute @Valid SuggestionsRequestDTO requestDTO
	                                                            ) {
		var cities = cityService.searchCities(requestDTO);
		SuggestionsResponseDTO response = new SuggestionsResponseDTO(cities);
		return ResponseEntity.ok(response);
	}
}
