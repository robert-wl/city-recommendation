package com.robert.codingchallenge.model.dto;

import com.robert.codingchallenge.model.data.City;

import java.util.List;

public record GetSuggestionsResponseDTO(
		List<City> suggestions
) {
}
