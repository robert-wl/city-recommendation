package com.robert.codingchallenge.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SuggestionsRequestDTO(
		@Schema(description = "The search query string for cities", nullable = false, example = "New York")
		@NotBlank(message = "Search query must not be null or empty")
		String q,

		@Schema(description = "Latitude for location-based search (optional)", nullable = true, example = "40.7128")
		@Min(value = -90, message = "Latitude must be between -90 and 90")
		@Max(value = 90, message = "Latitude must be between -90 and 90")
		Double latitude,

		@Schema(description = "Longitude for location-based search (optional)", nullable = true, example = "-74.0060")
		@Min(value = -180, message = "Longitude must be between -180 and 180")
		@Max(value = 180, message = "Longitude must be between -180 and 180")
		Double longitude,

		@Schema(description = "Algorithm to use for search 0 = Levenshtein, 1 = Jaro-Winkler, 2 = Jaccard", nullable = true, example = "0")
		@Min(value = 0, message = "Algorithm must be between 0 and 2")
		@Max(value = 2, message = "Algorithm must be between 0 and 2")
		Integer algorithm
) {
}
