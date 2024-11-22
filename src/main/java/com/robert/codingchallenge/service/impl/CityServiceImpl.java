package com.robert.codingchallenge.service.impl;

import com.robert.codingchallenge.model.City;
import com.robert.codingchallenge.repository.CityRepository;
import com.robert.codingchallenge.service.CityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CityServiceImpl implements CityService {
	private final CityRepository cityRepository;

	@Override
	public List<City> getAllCities() {
		return cityRepository.getAllCities();
	}
}
