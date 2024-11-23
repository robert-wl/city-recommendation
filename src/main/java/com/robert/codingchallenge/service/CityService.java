package com.robert.codingchallenge.service;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.util.search.SearchMatch;

import java.util.List;

public interface CityService {

	List<SearchMatch<City>> searchCities(String query);
}
