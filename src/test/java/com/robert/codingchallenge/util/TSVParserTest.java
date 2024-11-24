package com.robert.codingchallenge.util;

import com.robert.codingchallenge.model.data.City;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TSVParserTest {
	@InjectMocks
	private TSVParser tsvParser;

	@Test
	void testParse_validTSV() {
		String tsv = """
				name\talt_name\tlat\tlong\tcountry\ttz
				London\tlon,don,lo,nd,on\t51.5074\t-0.1278\tUnited Kingdom\tEurope/London
				Paris\tpar,ris,pa,ri,s\t48.8566\t2.3522\tFrance\tEurope/Paris
				""";

		Map<String, String> headerToField = Map.of(
				"name", "name",
				"alt_name", "altNames",
				"lat", "latitude",
				"long", "longitude",
				"country", "country",
				"tz", "tz"
		                                          );

		List<City> cities = tsvParser.parse(tsv, headerToField, City.class);

		assertNotEquals(0, cities.size(), "Cities should not be empty");
		assertEquals(2, cities.size(), "There should be 2 cities");
		assertEquals("London", cities.get(0).getName(), "First city should be London");
		assertEquals(List.of("lon", "don", "lo", "nd", "on"), cities.get(0).getAltNames(), "London should have alt names");
		assertEquals(51.5074, cities.get(0).getLatitude(), 0.0001, "London should have latitude 51.5074");
		assertEquals(-0.1278, cities.get(0).getLongitude(), 0.0001, "London should have longitude -0.1278");
		assertEquals("United Kingdom", cities.get(0).getCountry(), "London should be in United Kingdom");
		assertEquals("Europe/London", cities.get(0).getTz(), "London should be in Europe/London timezone");
	}

	@Test
	void testParse_emptyTSV() {
		String tsv = "";

		Map<String, String> headerToField = Map.of(
				"name", "name",
				"alt_name", "altNames",
				"lat", "latitude",
				"long", "longitude",
				"country", "country",
				"tz", "tz"
		                                          );

		List<City> cities = tsvParser.parse(tsv, headerToField, City.class);

		assertEquals(0, cities.size(), "Cities should be empty");
	}

	@Test
	void testParse_invalidHeaderToFieldMapping() {
		String tsv = """
				name\talt_name\tlat\tlong\tcountry\ttz
				London\tlon,don,lo,nd,on\t51.5074\t-0.1278\tUnited Kingdom\tEurope/London
				Paris\tpar,ris,pa,ri,s\t48.8566\t2.3522\tFrance\tEurope/Paris
				""";

		Map<String, String> headerToField = Map.of(
				"name", "name",
				"alt_name", "altNames",
				"lat", "latitude",
				"long", "longitude",
				"country", "country",
				"tz", "tz1"
		                                          );

		List<City> cities = tsvParser.parse(tsv, headerToField, City.class);

		assertTrue(cities.isEmpty(), "Cities should be empty");
	}

	@Test
	void testParse_invalidData() {
		String tsv = """
				t\t\t\t\t\tcountry\ttz
				London\t\t51.5074\t-0.1278\tUnited Kingdom\tEurope/London
				Paris\tpar,ris,pa,ri,s\t48.8566\t2.3522\tFrance\tEurope/Paris
				""";

		Map<String, String> headerToField = Map.of(
				"name", "name",
				"alt_name", "altNames",
				"lat", "latitude",
				"long", "longitude",
				"country", "country",
				"tz", "tz"
		                                          );

		List<City> cities = tsvParser.parse(tsv, headerToField, City.class);

		assertTrue(cities.isEmpty(), "Cities should be empty");
	}

	@Test
	void testParse_emptyFields() {
		String tsv = """
				name\talt_name\tlat\tlong\tcountry\ttz
				London\t\t\t\tUnited Kingdom\tEurope/London
				Paris\tpar,ris,pa,ri,s\t48.8566\t2.3522\tFrance\tEurope/Paris
				""";

		Map<String, String> headerToField = Map.of(
				"name", "name",
				"alt_name", "altNames",
				"lat", "latitude",
				"long", "longitude",
				"country", "country",
				"tz", "tz"
		                                          );

		List<City> cities = tsvParser.parse(tsv, headerToField, City.class);

		assertNotEquals(0, cities.size(), "Cities should not be empty");
		assertEquals(2, cities.size(), "There should be 2 cities");
		assertEquals("London", cities.get(0).getName(), "First city should be London");
		assertNull(cities.get(0).getAltNames(), "London should not have alt names");
		assertNull(cities.get(0).getLatitude(), "London should not have latitude");
		assertNull(cities.get(0).getLongitude(), "London should not have longitude");
		assertEquals("United Kingdom", cities.get(0).getCountry(), "London should be in United Kingdom");
		assertEquals("Europe/London", cities.get(0).getTz(), "London should be in Europe/London timezone");
	}
}
