package com.robert.codingchallenge.service.impl;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.repository.CityRepository;
import com.robert.codingchallenge.service.CityService;
import com.robert.codingchallenge.util.search.SearchMatch;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CityServiceImpl implements CityService {
	private final CityRepository cityRepository;
	

	@Override
	public List<SearchMatch<City>> searchCities(String query) {
		return cityRepository.getCitiesByName(query);
	}
}
