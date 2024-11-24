package com.robert.codingchallenge.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PaginationDTO(
		@Schema(description = "The page number to fetch", nullable = true, example = "1")
		@Min(value = 1, message = "Page number must be greater than or equal to 1")
		Integer page,

		@Schema(description = "The number of items per page", nullable = true, example = "10")
		@Min(value = 1, message = "Page size must be greater than or equal to 1")
		@Max(value = 100, message = "Page size must be less than or equal to 100")
		Integer pageSize
) {
}
