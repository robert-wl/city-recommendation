package com.robert.codingchallenge.model.dto.response;

public record ErrorResponseDTO(
		int statusCode,
		String error,
		Object message
) {
}

