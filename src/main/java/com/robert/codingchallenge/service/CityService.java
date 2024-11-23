package com.robert.codingchallenge.service;

import com.robert.codingchallenge.model.dto.ScoredCityDTO;

import java.util.List;

public interface CityService {

	List<ScoredCityDTO> searchCities(String query);

	List<ScoredCityDTO> searchCities(String query, Double latitude, Double longitude);
}
