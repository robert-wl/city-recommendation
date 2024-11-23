package com.robert.codingchallenge.util;

import com.robert.codingchallenge.model.data.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class TSVParserTest {
	@InjectMocks
	private TSVParser tsvParser;

	@Test
	void testParse_validTSV() {
		String tsv = "name\talt_name\tlat\tlong\tcountry\ttz\n" +
				"London\tlon,don,lo,nd,on\t51.5074\t-0.1278\tUnited Kingdom\tEurope/London\n" +
				"Paris\tpar,ris,pa,ri,s\t48.8566\t2.3522\tFrance\tEurope/Paris\n";

		Map<String, String> headerToField = Map.of(
				"name", "name",
				"alt_name", "altNames",
				"lat", "latitude",
				"long", "longitude",
				"country", "country",
				"tz", "tz"
		                                          );

		List<City> cities = tsvParser.parse(tsv, headerToField, City.class);

		Assertions.assertNotEquals(0, cities.size(), "Cities should not be empty");
		Assertions.assertEquals(2, cities.size(), "There should be 2 cities");
		Assertions.assertEquals("London", cities.get(0).getName(), "First city should be London");
		Assertions.assertEquals(List.of("lon", "don", "lo", "nd", "on"), cities.get(0).getAltNames(), "London should have alt names");
		Assertions.assertEquals(51.5074, cities.get(0).getLatitude(), 0.0001, "London should have latitude 51.5074");
		Assertions.assertEquals(-0.1278, cities.get(0).getLongitude(), 0.0001, "London should have longitude -0.1278");
		Assertions.assertEquals("United Kingdom", cities.get(0).getCountry(), "London should be in United Kingdom");
		Assertions.assertEquals("Europe/London", cities.get(0).getTz(), "London should be in Europe/London timezone");
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

		Assertions.assertEquals(0, cities.size(), "Cities should be empty");
	}

	@Test
	void testParse_invalidHeaderToFieldMapping() {
		String tsv = "name\talt_name\tlat\tlong\tcountry\ttz\n" +
				"London\tlon,don,lo,nd,on\t51.5074\t-0.1278\tUnited Kingdom\tEurope/London\n" +
				"Paris\tpar,ris,pa,ri,s\t48.8566\t2.3522\tFrance\tEurope/Paris\n";

		Map<String, String> headerToField = Map.of(
				"name", "name",
				"alt_name", "altNames",
				"lat", "latitude",
				"long", "longitude",
				"country", "country",
				"tz", "tz1"
		                                          );

		List<City> cities = tsvParser.parse(tsv, headerToField, City.class);

		Assertions.assertTrue(cities.isEmpty(), "Cities should be empty");
	}

	@Test
	void testParse_invalidData() {
		String tsv = "t\t\t\t\t\tcountry\ttz\n" +
				"London\t\t51.5074\t-0.1278\tUnited Kingdom\tEurope/London\n" +
				"Paris\tpar,ris,pa,ri,s\t48.8566\t2.3522\tFrance\tEurope/Paris\n";

		Map<String, String> headerToField = Map.of(
				"name", "name",
				"alt_name", "altNames",
				"lat", "latitude",
				"long", "longitude",
				"country", "country",
				"tz", "tz"
		                                          );

		List<City> cities = tsvParser.parse(tsv, headerToField, City.class);

		Assertions.assertTrue(cities.isEmpty(), "Cities should be empty");
	}

	@Test
	void testParse_emptyFields() {
		String tsv = "name\talt_name\tlat\tlong\tcountry\ttz\n" +
				"London\t\t\t\tUnited Kingdom\tEurope/London\n" +
				"Paris\tpar,ris,pa,ri,s\t48.8566\t2.3522\tFrance\tEurope/Paris\n";

		Map<String, String> headerToField = Map.of(
				"name", "name",
				"alt_name", "altNames",
				"lat", "latitude",
				"long", "longitude",
				"country", "country",
				"tz", "tz"
		                                          );

		List<City> cities = tsvParser.parse(tsv, headerToField, City.class);

		Assertions.assertNotEquals(0, cities.size(), "Cities should not be empty");
		Assertions.assertEquals(2, cities.size(), "There should be 2 cities");
		Assertions.assertEquals("London", cities.get(0).getName(), "First city should be London");
		Assertions.assertNull(cities.get(0).getAltNames(), "London should not have alt names");
		Assertions.assertNull(cities.get(0).getLatitude(), "London should not have latitude");
		Assertions.assertNull(cities.get(0).getLongitude(), "London should not have longitude");
		Assertions.assertEquals("United Kingdom", cities.get(0).getCountry(), "London should be in United Kingdom");
		Assertions.assertEquals("Europe/London", cities.get(0).getTz(), "London should be in Europe/London timezone");
	}
}
