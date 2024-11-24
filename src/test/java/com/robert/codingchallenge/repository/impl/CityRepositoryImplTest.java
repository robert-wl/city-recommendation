package com.robert.codingchallenge.repository.impl;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.util.fuzzysearch.SearchMatch;
import com.robert.codingchallenge.util.fuzzysearch.impl.CityFuzzySearch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CityRepositoryImplTest {
	@Mock
	private CityFuzzySearch cityFuzzySearch;

	@InjectMocks
	private CityRepositoryImpl cityRepository;

	@Test
	void testGetCitiesByName_shouldReturnMatchingCities() {
		City city1 = mock(City.class);
		when(city1.getName()).thenReturn("City1");

		City city2 = mock(City.class);
		when(city2.getName()).thenReturn("City2");

		SearchMatch<City> searchMatch1 = mock(SearchMatch.class);
		when(searchMatch1.getData()).thenReturn(city1);
		when(searchMatch1.getScore()).thenReturn(0.9);

		SearchMatch<City> searchMatch2 = mock(SearchMatch.class);
		when(searchMatch2.getData()).thenReturn(city2);
		when(searchMatch2.getScore()).thenReturn(0.7);

		when(cityFuzzySearch.search("City")).thenReturn(List.of(searchMatch1, searchMatch2));

		List<SearchMatch<City>> result = cityRepository.getCitiesByName("City");

		assertEquals(2, result.size());
		assertEquals("City1", result.get(0).getData().getName());
		assertEquals(0.9, result.get(0).getScore());
		assertEquals("City2", result.get(1).getData().getName());
		assertEquals(0.7, result.get(1).getScore());
	}
}
