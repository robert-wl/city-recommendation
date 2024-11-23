package com.robert.codingchallenge.service;

import com.robert.codingchallenge.model.data.ScoredCity;

import java.util.List;

public interface CityService {

	List<ScoredCity> searchCities(String query);

	List<ScoredCity> searchCities(String query, Double latitude, Double longitude);
}
