package com.robert.codingchallenge.model.data;

public record ScoredCity(
		String name,
		Double latitude,
		Double longitude,
		Double score
) {
}
