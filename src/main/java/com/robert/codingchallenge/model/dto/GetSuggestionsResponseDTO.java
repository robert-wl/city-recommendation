package com.robert.codingchallenge.model.dto;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.util.search.SearchMatch;

import java.util.List;

public record GetSuggestionsResponseDTO(
		List<SearchMatch<City>> suggestions
) {
}
