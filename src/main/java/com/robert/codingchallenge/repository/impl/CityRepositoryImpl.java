package com.robert.codingchallenge.repository.impl;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.repository.CityRepository;
import com.robert.codingchallenge.util.FileReader;
import com.robert.codingchallenge.util.TSVParser;
import com.robert.codingchallenge.util.search.SearchMatch;
import com.robert.codingchallenge.util.search.impl.CityFuzzySearch;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Repository
public class CityRepositoryImpl implements CityRepository {
	private final String DATA_FILE = "cities_canada-usa.tsv";
	private final CityFuzzySearch cities;
	private final TSVParser tsvParser;
	private final FileReader fileReader;


	@PostConstruct
	public void init() {
		loadData();
	}

	private void loadData() {
		Map<String, String> headerToField = Map.of(
				"name", "name",
				"alt_name", "altNames",
				"lat", "latitude",
				"long", "longitude",
				"country", "country",
				"tz", "tz"
		                                          );

		String tsvContent = fileReader.read(DATA_FILE);
		List<City> data = tsvParser.parse(tsvContent, headerToField, City.class);

		log.info("Loaded {} cities", data.size());

		data.forEach(cities::add);
	}

	@Override
	public List<SearchMatch<City>> getCitiesByName(String q) {
		return cities.search(q);
	}
}
