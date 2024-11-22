package com.robert.codingchallenge.repository.impl;

import com.robert.codingchallenge.model.City;
import com.robert.codingchallenge.repository.CityRepository;
import com.robert.codingchallenge.util.TSVParser;
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
	private final List<City> cities;
	private final TSVParser tsvParser;


	@PostConstruct
	public void init() {
		loadData();
	}

	private void loadData() {
		Map<String, String> headerToField = Map.of(
				"name", "name",
				"alt_name", "altNames",
				"lat", "latitude",
				"long", "longitude"
		                                          );
		List<City> data = tsvParser.parse(DATA_FILE, headerToField, City.class);
		cities.addAll(data);
		log.info("Loaded {} cities", cities.size());
	}
}
