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

		Assertions.assertNotEquals(0, cities.size());
		Assertions.assertEquals(2, cities.size());
		Assertions.assertEquals("London", cities.get(0).getName());
		Assertions.assertEquals(List.of("lon", "don", "lo", "nd", "on"), cities.get(0).getAltNames());
		Assertions.assertEquals(51.5074, cities.get(0).getLatitude());
		Assertions.assertEquals(-0.1278, cities.get(0).getLongitude());
		Assertions.assertEquals("United Kingdom", cities.get(0).getCountry());
		Assertions.assertEquals("Europe/London", cities.get(0).getTz());
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

		Assertions.assertEquals(0, cities.size());
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

		Assertions.assertTrue(cities.isEmpty());
	}

	@Test
	void testParse_invalidData() {
		String tsv = "name\talt_name\tlat\tlong\tcountry\ttz\n" +
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

		Assertions.assertTrue(cities.isEmpty());
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

		Assertions.assertNotEquals(0, cities.size());
		Assertions.assertEquals(2, cities.size());
		Assertions.assertEquals("London", cities.get(0).getName());
		Assertions.assertNull(cities.get(0).getAltNames());
		Assertions.assertNull(cities.get(0).getLatitude());
		Assertions.assertNull(cities.get(0).getLongitude());
		Assertions.assertEquals("United Kingdom", cities.get(0).getCountry());
		Assertions.assertEquals("Europe/London", cities.get(0).getTz());
	}
}
