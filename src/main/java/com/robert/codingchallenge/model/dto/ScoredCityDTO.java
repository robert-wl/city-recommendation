package com.robert.codingchallenge.model.dto;

public record ScoredCityDTO(
		String name,
		Double latitude,
		Double longitude,
		Double score
) {
}
