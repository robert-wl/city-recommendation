package com.robert.codingchallenge.util.fuzzysearch.impl;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.util.fuzzysearch.SearchMatch;
import com.robert.codingchallenge.util.gramindex.impl.CityIndex;
import com.robert.codingchallenge.util.stringcomparator.StringAlgorithm;
import com.robert.codingchallenge.util.stringcomparator.StringComparator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class CityFuzzySearchTest {

	@InjectMocks
	private CityFuzzySearch cityFuzzySearch;
	@Mock
	private CityIndex cityIndex;
	@Mock
	private StringComparator stringComparator;

	private City city1;
	private City city2;

	@BeforeEach
	void setUp() {
		city1 = City.builder()
				.name("London")
				.altNames(List.of("Londres"))
				.country("UK")
				.tz("GMT")
				.latitude(51.5074)
				.longitude(-0.1278)
				.build();

		city2 = City.builder()
				.name("Paris")
				.altNames(List.of("París"))
				.country("France")
				.tz("CET")
				.latitude(48.8566)
				.longitude(2.3522)
				.build();
	}

	@Test
	void testSearch_noMatches() {
		Mockito.when(cityIndex.search("Berlin")).thenReturn(Set.of());

		List<SearchMatch<City>> results = cityFuzzySearch.search("Berlin");

		Assertions.assertTrue(results.isEmpty(), "Expected no search results");
	}

	@Test
	void testSearch_withMatch() {
		Set<City> cities = new HashSet<>();
		cities.add(city1);
		Mockito.when(cityIndex.search("London")).thenReturn(cities);
		Mockito.when(stringComparator.compare(city1.getName().toLowerCase(), "London".toLowerCase(), StringAlgorithm.JACCARD))
				.thenReturn(1.0);

		List<SearchMatch<City>> results = cityFuzzySearch.search("London");

		Assertions.assertEquals(1, results.size(), "Expected 1 search result");
		SearchMatch<City> match = results.get(0);
		Assertions.assertEquals(city1, match.getData(), "Expected the correct city match");
		Assertions.assertEquals(1.0, match.getScore(), 0.01, "Expected score to be 1.0 for perfect match");
	}

	@Test
	void testSearch_withLowScore() {
		Set<City> cities = new HashSet<>();
		cities.add(city1);
		Mockito.when(cityIndex.search("London")).thenReturn(cities);
		Mockito.when(stringComparator.compare(city1.getName().toLowerCase(), "London".toLowerCase(), StringAlgorithm.JACCARD))
				.thenReturn(0.4);

		List<SearchMatch<City>> results = cityFuzzySearch.search("London");

		Assertions.assertTrue(results.isEmpty(), "Expected no results since the score is below the threshold");
	}

	@Test
	void testSearch_withFilteredScore() {
		Set<City> cities = new HashSet<>();
		cities.add(city1);
		cities.add(city2);

		Mockito.when(cityIndex.search("Lon")).thenReturn(cities);
		Mockito.when(stringComparator.compare(city1.getName().toLowerCase(), "Lon".toLowerCase(), StringAlgorithm.JACCARD))
				.thenReturn(0.7);
		Mockito.when(stringComparator.compare(city2.getName().toLowerCase(), "Lon".toLowerCase(), StringAlgorithm.JACCARD))
				.thenReturn(0.4);

		List<SearchMatch<City>> results = cityFuzzySearch.search("Lon");

		Assertions.assertEquals(1, results.size(), "Expected 1 search result");
		Assertions.assertEquals(city1, results.get(0).getData(), "Expected only one valid result (London)");
	}

	@Test
	void testSearch_withMultipleMatches() {
		Set<City> cities = new HashSet<>();
		cities.add(city1);
		cities.add(city2);

		Mockito.when(cityIndex.search("Lon")).thenReturn(cities);
		Mockito.when(stringComparator.compare(city1.getName().toLowerCase(), "Lon".toLowerCase(), StringAlgorithm.JACCARD))
				.thenReturn(0.9);
		Mockito.when(stringComparator.compare(city2.getName().toLowerCase(), "Lon".toLowerCase(), StringAlgorithm.JACCARD))
				.thenReturn(0.7);

		List<SearchMatch<City>> results = cityFuzzySearch.search("Lon");

		Assertions.assertEquals(2, results.size(), "Expected 2 search results");
		Assertions.assertTrue(results.get(0).getScore() >= results.get(1).getScore(), "Results should be sorted by score");
	}
}
