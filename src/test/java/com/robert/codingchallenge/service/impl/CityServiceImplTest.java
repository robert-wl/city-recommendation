package com.robert.codingchallenge.service.impl;

import com.robert.codingchallenge.mapper.CityMapper;
import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.model.dto.ScoredCityDTO;
import com.robert.codingchallenge.repository.CityRepository;
import com.robert.codingchallenge.util.GeoCalculator;
import com.robert.codingchallenge.util.fuzzysearch.SearchMatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CityServiceImplTest {
	@Mock
	private CityRepository cityRepository;
	@Mock
	private GeoCalculator geoCalculator;
	@Mock
	private CityMapper cityMapper;

	@InjectMocks
	private CityServiceImpl cityService;


	@Test
	void testSearchCities_shouldReturnMappedCities() {
		SearchMatch<City> searchMatch1 = Mockito.mock(SearchMatch.class);

		SearchMatch<City> searchMatch2 = Mockito.mock(SearchMatch.class);

		List<SearchMatch<City>> mockCities = List.of(searchMatch1, searchMatch2);

		ScoredCityDTO scoredCityDTO1 = Mockito.mock(ScoredCityDTO.class);
		Mockito.when(scoredCityDTO1.getName()).thenReturn("City1");
		Mockito.when(scoredCityDTO1.getScore()).thenReturn(1.0);

		ScoredCityDTO scoredCityDTO2 = Mockito.mock(ScoredCityDTO.class);
		Mockito.when(scoredCityDTO2.getName()).thenReturn("City2");
		Mockito.when(scoredCityDTO2.getScore()).thenReturn(1.0);

		Mockito.when(cityRepository.getCitiesByName("City")).thenReturn(mockCities);
		Mockito.when(cityMapper.toScoredCities(mockCities)).thenReturn(List.of(scoredCityDTO1, scoredCityDTO2));

		List<ScoredCityDTO> result = cityService.searchCities("City");

		Assertions.assertEquals(2, result.size());
		Assertions.assertEquals("City1", result.get(0).getName());
		Assertions.assertEquals("City2", result.get(1).getName());
		Mockito.verify(cityRepository, Mockito.times(1)).getCitiesByName("City");
		Mockito.verify(cityMapper, Mockito.times(1)).toScoredCities(mockCities);
	}

	@Test
	void testSearchCitiesWithLatLong_shouldReturnMappedCitiesWithGeoScore() {
		SearchMatch<City> searchMatch1 = Mockito.mock(SearchMatch.class);
		SearchMatch<City> searchMatch2 = Mockito.mock(SearchMatch.class);

		List<SearchMatch<City>> mockCities = List.of(searchMatch1, searchMatch2);

		ScoredCityDTO scoredCityDTO1 = ScoredCityDTO.builder()
				.name("City1")
				.latitude(45.0)
				.longitude(90.0)
				.score(0.9)
				.build();

		ScoredCityDTO scoredCityDTO2 = ScoredCityDTO.builder()
				.name("City2")
				.latitude(50.0)
				.longitude(85.0)
				.score(0.8)
				.build();

		Mockito.when(geoCalculator.score(45.0, 90.0, 46.0, 91.0)).thenReturn(0.1);
		Mockito.when(geoCalculator.score(50.0, 85.0, 46.0, 91.0)).thenReturn(0.05);

		Mockito.when(cityRepository.getCitiesByName("City")).thenReturn(mockCities);
		Mockito.when(cityMapper.toScoredCities(mockCities)).thenReturn(List.of(scoredCityDTO1, scoredCityDTO2));

		List<ScoredCityDTO> result = cityService.searchCities("City", 46.0, 91.0);

		Assertions.assertEquals(2, result.size());
		Assertions.assertEquals("City1", result.get(0).getName());
		Assertions.assertEquals("City2", result.get(1).getName());
		Assertions.assertTrue(result.get(0).getScore() > result.get(1).getScore());  // City1 should have higher score

		Mockito.verify(cityRepository, Mockito.times(1)).getCitiesByName("City");
		Mockito.verify(cityMapper, Mockito.times(1)).toScoredCities(mockCities);
		Mockito.verify(geoCalculator, Mockito.times(2)).score(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.eq(46.0), Mockito.eq(91.0));
	}

	@Test
	void testSearchCitiesWithInvalidLatitudeLongitude_shouldThrowException() {
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			cityService.searchCities("City", 45.0, null);
		});

		Assertions.assertEquals("Longitude must be provided if latitude is provided", thrown.getMessage());
	}

	@Test
	void testSearchCitiesWithInvalidLatitudeLongitude_shouldThrowException2() {
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			cityService.searchCities("City", null, 90.0);
		});


		Assertions.assertEquals("Latitude must be provided if longitude is provided", thrown.getMessage());
	}
}
