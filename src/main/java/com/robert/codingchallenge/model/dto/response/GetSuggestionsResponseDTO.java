package com.robert.codingchallenge.model.dto.response;

import com.robert.codingchallenge.model.dto.ScoredCityDTO;

import java.util.List;

public record GetSuggestionsResponseDTO(
		List<ScoredCityDTO> suggestions
) {
}
