package com.robert.codingchallenge.model.dto.response;

import java.util.Map;

public record ErrorResponseDTO(
		int statusCode,
		String error,
		Map<String, String> message
) {
}

