package com.robert.codingchallenge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class ScoredCityDTO {
	private String name;
	private Double latitude;
	private Double longitude;
	private Double score;
}
