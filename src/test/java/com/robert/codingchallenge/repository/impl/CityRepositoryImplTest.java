package com.robert.codingchallenge.repository.impl;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.util.FileReader;
import com.robert.codingchallenge.util.TSVParser;
import com.robert.codingchallenge.util.fuzzysearch.SearchMatch;
import com.robert.codingchallenge.util.fuzzysearch.impl.CityFuzzySearch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CityRepositoryImplTest {
	@Mock
	private FileReader fileReader;

	@Mock
	private TSVParser tsvParser;

	@Mock
	private CityFuzzySearch cityFuzzySearch;

	@InjectMocks
	private CityRepositoryImpl cityRepository;

	@Test
	void testGetCitiesByName_shouldReturnMatchingCities() {
		City city1 = Mockito.mock(City.class);
		Mockito.when(city1.getName()).thenReturn("City1");

		City city2 = Mockito.mock(City.class);
		Mockito.when(city2.getName()).thenReturn("City2");

		SearchMatch<City> searchMatch1 = Mockito.mock(SearchMatch.class);
		Mockito.when(searchMatch1.getData()).thenReturn(city1);
		Mockito.when(searchMatch1.getScore()).thenReturn(0.9);

		SearchMatch<City> searchMatch2 = Mockito.mock(SearchMatch.class);
		Mockito.when(searchMatch2.getData()).thenReturn(city2);
		Mockito.when(searchMatch2.getScore()).thenReturn(0.7);

		Mockito.when(cityFuzzySearch.search("City")).thenReturn(List.of(searchMatch1, searchMatch2));

		List<SearchMatch<City>> result = cityRepository.getCitiesByName("City");

		Assertions.assertEquals(2, result.size());
		Assertions.assertEquals("City1", result.get(0).getData().getName());
		Assertions.assertEquals(0.9, result.get(0).getScore());
		Assertions.assertEquals("City2", result.get(1).getData().getName());
		Assertions.assertEquals(0.7, result.get(1).getScore());
	}
}
