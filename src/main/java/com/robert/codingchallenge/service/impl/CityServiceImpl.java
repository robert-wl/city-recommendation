package com.robert.codingchallenge.service.impl;

import com.robert.codingchallenge.model.data.City;
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


	@Override
	@Cacheable(value = "cities", key = "#query")
	public List<SearchMatch<City>> searchCities(String query) {
		return cityRepository.getCitiesByName(query);
	}

	private void calculateScoreWithLatAndLong(SearchMatch<City> cityMatch, Double latitude, Double longitude) {
		if (latitude == null || longitude == null) {
			return;
		}

		final double scoreRatio = 0.7;

		double newScore = cityMatch.getScore() * (1 - scoreRatio);

		City city = cityMatch.getData();
		double geoScore = geoCalculator.score(
				city.getLatitude(), city.getLongitude(),
				latitude, longitude
		                                     );

		newScore += geoScore * scoreRatio;


		cityMatch.setScore(newScore);
	}

	@Override
	@Cacheable(value = "cities", key = "#query + '|' + #latitude + '|' + #longitude")
	public List<SearchMatch<City>> searchCities(String query, Double latitude, Double longitude) {
		if (latitude != null && longitude == null) {
			throw new IllegalArgumentException("Longitude must be provided if latitude is provided");
		}

		if (longitude != null && latitude == null) {
			throw new IllegalArgumentException("Latitude must be provided if longitude is provided");
		}

		return searchCities(query).stream()
				.peek(match -> calculateScoreWithLatAndLong(match, latitude, longitude))
				.filter(m -> m.getScore() != 0)
				.sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
				.collect(Collectors.toList());
	}
}
