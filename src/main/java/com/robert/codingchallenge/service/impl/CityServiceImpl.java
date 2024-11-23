package com.robert.codingchallenge.service.impl;

import com.robert.codingchallenge.mapper.CityMapper;
import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.model.dto.ScoredCityDTO;
import com.robert.codingchallenge.repository.CityRepository;
import com.robert.codingchallenge.service.CityService;
import com.robert.codingchallenge.util.GeoCalculator;
import com.robert.codingchallenge.util.search.SearchMatch;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class CityServiceImpl implements CityService {
	private final CityRepository cityRepository;

	private final GeoCalculator geoCalculator;

	private final CityMapper cityMapper;

	@Override
	@Cacheable(value = "cities", key = "#query")
	public List<ScoredCityDTO> searchCities(String query) {
		List<SearchMatch<City>> cities = cityRepository.getCitiesByName(query);

		return cityMapper.toScoredCities(cities);
	}

	private ScoredCityDTO calculateFinalScore(ScoredCityDTO city, Double latitude, Double longitude) {
		if (latitude == null || longitude == null) {
			return city;
		}

		final double scoreRatio = 0.7;

		double newScore = city.getScore() * (1 - scoreRatio);

		double geoScore = geoCalculator.score(
				city.getLatitude(), city.getLongitude(),
				latitude, longitude
		                                     );

		newScore += geoScore * scoreRatio;

		return city.toBuilder()
				.score(newScore)
				.build();
	}

	@Override
	@Cacheable(value = "cities", key = "#query + '|' + #latitude + '|' + #longitude")
	public List<ScoredCityDTO> searchCities(String query, Double latitude, Double longitude) {
		if (latitude != null && longitude == null) {
			throw new IllegalArgumentException("Longitude must be provided if latitude is provided");
		}

		if (longitude != null && latitude == null) {
			throw new IllegalArgumentException("Latitude must be provided if longitude is provided");
		}

		return searchCities(query).stream()
				.map(match -> calculateFinalScore(match, latitude, longitude))
				.filter(m -> m.getScore() != 0)
				.sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
				.collect(Collectors.toList());
	}
}
