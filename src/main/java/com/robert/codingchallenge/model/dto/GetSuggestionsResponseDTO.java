package com.robert.codingchallenge.model.dto;

import com.robert.codingchallenge.model.data.ScoredCity;

import java.util.List;

public record GetSuggestionsResponseDTO(
		List<ScoredCity> suggestions
) {
}
