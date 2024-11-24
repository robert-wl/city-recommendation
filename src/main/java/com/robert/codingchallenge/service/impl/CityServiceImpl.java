package com.robert.codingchallenge.service.impl;

import com.robert.codingchallenge.mapper.CityMapper;
import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.model.dto.ScoredCityDTO;
import com.robert.codingchallenge.model.dto.request.PaginationDTO;
import com.robert.codingchallenge.model.dto.request.SuggestionsRequestDTO;
import com.robert.codingchallenge.repository.CityRepository;
import com.robert.codingchallenge.service.CityService;
import com.robert.codingchallenge.util.GeoCalculator;
import com.robert.codingchallenge.util.fuzzysearch.SearchMatch;
import com.robert.codingchallenge.util.stringcomparator.StringAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

		return cityMapper.toScoredCities(cities).stream()
				.filter(m -> m.getScore() > 0)
				.sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
				.collect(Collectors.toList());
	}

	private ScoredCityDTO calculateLatitudeLongitude(ScoredCityDTO city, Double latitude, Double longitude) {
		if (latitude == null && longitude == null) {
			return city;
		}

		if (latitude == null ^ longitude == null) {
			throw new IllegalArgumentException("Longitude and latitude must be provided together");
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


		List<SearchMatch<City>> cities = cityRepository.getCitiesByName(query);

		return cityMapper.toScoredCities(cities).stream()
				.map(match -> calculateLatitudeLongitude(match, latitude, longitude))
				.filter(m -> m.getScore() > 0)
				.sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
				.collect(Collectors.toList());
	}

	@Override
	@Cacheable(value = "cities", key = "#dto.q() + '|' + #dto.latitude() + '|' + #dto.longitude() + '|' + #dto.algorithm() + '|' + #dto.minPopulation() + '|' + #dto.maxPopulation()")
	public List<ScoredCityDTO> searchCities(SuggestionsRequestDTO dto) {
		String query = dto.q();

		List<SearchMatch<City>> result = Optional.ofNullable(dto.algorithm())
				.map(algorithm -> cityRepository.getCitiesByName(query, StringAlgorithm.of(algorithm)))
				.orElseGet(() -> cityRepository.getCitiesByName(query));


		if (dto.minPopulation() != null) {
			result = result.stream()
					.filter(c -> c.getData().getPopulation() >= dto.minPopulation())
					.toList();
		}

		if (dto.maxPopulation() != null) {
			result = result.stream()
					.filter(c -> c.getData().getPopulation() <= dto.maxPopulation())
					.toList();
		}

		List<ScoredCityDTO> cities = cityMapper.toScoredCities(result);

		if (dto.latitude() != null || dto.longitude() != null) {
			cities = cities.stream()
					.map(c -> calculateLatitudeLongitude(c, dto.latitude(), dto.longitude()))
					.toList();
		}

		return cities.stream()
				.filter(m -> m.getScore() > 0)
				.sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
				.collect(Collectors.toList());
	}

	@Override
	public List<ScoredCityDTO> searchCitiesPaginated(SuggestionsRequestDTO dto, PaginationDTO paginationDTO) {
		if (paginationDTO.page() == null ^ paginationDTO.pageSize() == null) {
			throw new IllegalArgumentException("Both page and pageSize must be provided");
		}

		List<ScoredCityDTO> cities = searchCities(dto);

		if (paginationDTO.page() == null) {
			return cities;
		}

		int page = paginationDTO.page();
		int pageSize = paginationDTO.pageSize();


		return cities.stream()
				.skip((long) (page - 1) * pageSize)
				.limit(pageSize)
				.collect(Collectors.toList());
	}
}
