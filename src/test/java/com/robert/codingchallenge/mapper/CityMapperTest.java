package com.robert.codingchallenge.mapper;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.model.dto.ScoredCityDTO;
import com.robert.codingchallenge.util.fuzzysearch.SearchMatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CityMapperTest {
	@InjectMocks
	private CityMapper cityMapper;

	@Test
	void testToScoredCity() {
		City city = mock(City.class);
		when(city.getName()).thenReturn("Test City");
		when(city.getLatitude()).thenReturn(45.0);
		when(city.getLongitude()).thenReturn(90.0);


		SearchMatch<City> searchMatch = mock(SearchMatch.class);
		when(searchMatch.getData()).thenReturn(city);
		when(searchMatch.getScore()).thenReturn(0.8);

		ScoredCityDTO result = cityMapper.toScoredCity(searchMatch);

		assertEquals("Test City", result.getName());
		assertEquals(45.0, result.getLatitude());
		assertEquals(90.0, result.getLongitude());
		assertEquals(0.8, result.getScore());
	}

	@Test
	void testToScoredCities() {
		City city1 = mock(City.class);
		when(city1.getName()).thenReturn("City1");
		when(city1.getLatitude()).thenReturn(40.0);
		when(city1.getLongitude()).thenReturn(75.0);

		City city2 = mock(City.class);
		when(city2.getName()).thenReturn("City2");
		when(city2.getLatitude()).thenReturn(50.0);
		when(city2.getLongitude()).thenReturn(85.0);

		SearchMatch<City> searchMatch1 = mock(SearchMatch.class);
		when(searchMatch1.getData()).thenReturn(city1);
		when(searchMatch1.getScore()).thenReturn(0.9);

		SearchMatch<City> searchMatch2 = mock(SearchMatch.class);
		when(searchMatch2.getData()).thenReturn(city2);
		when(searchMatch2.getScore()).thenReturn(0.7);

		List<SearchMatch<City>> searchMatches = List.of(searchMatch1, searchMatch2);

		List<ScoredCityDTO> results = cityMapper.toScoredCities(searchMatches);

		assertEquals(2, results.size());

		ScoredCityDTO result1 = results.get(0);
		assertEquals("City1", result1.getName());
		assertEquals(40.0, result1.getLatitude());
		assertEquals(75.0, result1.getLongitude());
		assertEquals(0.9, result1.getScore());

		ScoredCityDTO result2 = results.get(1);
		assertEquals("City2", result2.getName());
		assertEquals(50.0, result2.getLatitude());
		assertEquals(85.0, result2.getLongitude());
		assertEquals(0.7, result2.getScore());
	}
}
