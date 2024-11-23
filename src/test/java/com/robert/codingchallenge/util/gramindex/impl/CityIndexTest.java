package com.robert.codingchallenge.util.gramindex.impl;

import com.robert.codingchallenge.model.data.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class CityIndexTest {

	private CityIndex cityIndex;

	@BeforeEach
	void setUp() {
		cityIndex = new CityIndex(3);
	}

	@Test
	void testAdd() {
		City city = Mockito.mock(City.class);
		Mockito.when(city.getName()).thenReturn("London");
		cityIndex.add(city);

		Set<City> result = cityIndex.search("London");
		Assertions.assertTrue(result.contains(city));
	}

	@Test
	void testSearch_noMatch() {
		City city = Mockito.mock(City.class);
		Mockito.when(city.getName()).thenReturn("London");
		cityIndex.add(city);

		Set<City> result = cityIndex.search("Paris");
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	void testSearch_withMatch() {
		City city = Mockito.mock(City.class);
		Mockito.when(city.getName()).thenReturn("London");
		cityIndex.add(city);

		Set<City> result = cityIndex.search("Lon");
		Assertions.assertTrue(result.contains(city));
	}

	@Test
	void testSearch_withMultipleMatches() {
		City city1 = Mockito.mock(City.class);
		City city2 = Mockito.mock(City.class);
		Mockito.when(city1.getName()).thenReturn("London");
		Mockito.when(city2.getName()).thenReturn("Londres");
		cityIndex.add(city1);
		cityIndex.add(city2);

		Set<City> result = cityIndex.search("Lon");
		Assertions.assertTrue(result.contains(city1));
		Assertions.assertTrue(result.contains(city2));
	}

	@Test
	void testSearch_withQSizeGreaterThanQuery() {
		City city = Mockito.mock(City.class);
		Mockito.when(city.getName()).thenReturn("London");
		cityIndex.add(city);

		Set<City> result = cityIndex.search("Lo");
		Assertions.assertTrue(result.contains(city));
	}
}
