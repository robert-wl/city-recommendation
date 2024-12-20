package com.robert.codingchallenge.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class City {
	private String name;
	private List<String> altNames;
	private String country;
	private String tz;
	private Double latitude;
	private Double longitude;
	private Integer population;
}
