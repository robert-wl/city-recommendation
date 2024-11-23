package com.robert.codingchallenge.mapper;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.model.dto.ScoredCityDTO;
import com.robert.codingchallenge.util.fuzzysearch.SearchMatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CityMapperTest {
	@InjectMocks
	private CityMapper cityMapper;

	@Test
	void testToScoredCity() {
		City city = Mockito.mock(City.class);
		Mockito.when(city.getName()).thenReturn("Test City");
		Mockito.when(city.getLatitude()).thenReturn(45.0);
		Mockito.when(city.getLongitude()).thenReturn(90.0);


		SearchMatch<City> searchMatch = Mockito.mock(SearchMatch.class);
		Mockito.when(searchMatch.getData()).thenReturn(city);
		Mockito.when(searchMatch.getScore()).thenReturn(0.8);

		ScoredCityDTO result = cityMapper.toScoredCity(searchMatch);

		Assertions.assertEquals("Test City", result.getName());
		Assertions.assertEquals(45.0, result.getLatitude());
		Assertions.assertEquals(90.0, result.getLongitude());
		Assertions.assertEquals(0.8, result.getScore());
	}

	@Test
	void testToScoredCities() {
		City city1 = Mockito.mock(City.class);
		Mockito.when(city1.getName()).thenReturn("City1");
		Mockito.when(city1.getLatitude()).thenReturn(40.0);
		Mockito.when(city1.getLongitude()).thenReturn(75.0);

		City city2 = Mockito.mock(City.class);
		Mockito.when(city2.getName()).thenReturn("City2");
		Mockito.when(city2.getLatitude()).thenReturn(50.0);
		Mockito.when(city2.getLongitude()).thenReturn(85.0);

		SearchMatch<City> searchMatch1 = Mockito.mock(SearchMatch.class);
		Mockito.when(searchMatch1.getData()).thenReturn(city1);
		Mockito.when(searchMatch1.getScore()).thenReturn(0.9);

		SearchMatch<City> searchMatch2 = Mockito.mock(SearchMatch.class);
		Mockito.when(searchMatch2.getData()).thenReturn(city2);
		Mockito.when(searchMatch2.getScore()).thenReturn(0.7);

		List<SearchMatch<City>> searchMatches = List.of(searchMatch1, searchMatch2);

		List<ScoredCityDTO> results = cityMapper.toScoredCities(searchMatches);

		Assertions.assertEquals(2, results.size());

		ScoredCityDTO result1 = results.get(0);
		Assertions.assertEquals("City1", result1.getName());
		Assertions.assertEquals(40.0, result1.getLatitude());
		Assertions.assertEquals(75.0, result1.getLongitude());
		Assertions.assertEquals(0.9, result1.getScore());

		ScoredCityDTO result2 = results.get(1);
		Assertions.assertEquals("City2", result2.getName());
		Assertions.assertEquals(50.0, result2.getLatitude());
		Assertions.assertEquals(85.0, result2.getLongitude());
		Assertions.assertEquals(0.7, result2.getScore());
	}
}
