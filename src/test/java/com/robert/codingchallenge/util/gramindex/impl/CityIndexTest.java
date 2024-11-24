package com.robert.codingchallenge.util.gramindex.impl;

import com.robert.codingchallenge.model.data.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CityIndexTest {

	private CityIndex cityIndex;

	@BeforeEach
	void setUp() {
		cityIndex = new CityIndex(3);
	}

	@Test
	void testAdd() {
		City city = mock(City.class);
		when(city.getName()).thenReturn("London");
		cityIndex.add(city);

		Set<City> result = cityIndex.search("London");
		assertTrue(result.contains(city));
	}

	@Test
	void testSearch_noMatch() {
		City city = mock(City.class);
		when(city.getName()).thenReturn("London");
		cityIndex.add(city);

		Set<City> result = cityIndex.search("Paris");
		assertTrue(result.isEmpty());
	}

	@Test
	void testSearch_withMatch() {
		City city = mock(City.class);
		when(city.getName()).thenReturn("London");
		cityIndex.add(city);

		Set<City> result = cityIndex.search("Lon");
		assertTrue(result.contains(city));
	}

	@Test
	void testSearch_withMultipleMatches() {
		City city1 = mock(City.class);
		City city2 = mock(City.class);
		when(city1.getName()).thenReturn("London");
		when(city2.getName()).thenReturn("Londres");
		cityIndex.add(city1);
		cityIndex.add(city2);

		Set<City> result = cityIndex.search("Lon");
		assertTrue(result.contains(city1));
		assertTrue(result.contains(city2));
	}

	@Test
	void testSearch_withQSizeGreaterThanQuery() {
		City city = mock(City.class);
		when(city.getName()).thenReturn("London");
		cityIndex.add(city);

		Set<City> result = cityIndex.search("Lo");
		assertTrue(result.contains(city));
	}
}
