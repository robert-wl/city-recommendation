package com.robert.codingchallenge.mapper;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.model.dto.ScoredCityDTO;
import com.robert.codingchallenge.util.search.SearchMatch;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CityMapper {

	public ScoredCityDTO toScoredCity(SearchMatch<City> city) {
		return ScoredCityDTO.builder()
				.name(city.getData().getName())
				.latitude(city.getData().getLatitude())
				.longitude(city.getData().getLongitude())
				.score(city.getScore())
				.build();
	}

	public List<ScoredCityDTO> toScoredCities(List<SearchMatch<City>> cities) {
		return cities.stream()
				.map(this::toScoredCity)
				.collect(Collectors.toList());
	}

}
