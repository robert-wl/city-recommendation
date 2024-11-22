package com.robert.codingchallenge.repository;

import com.robert.codingchallenge.model.data.City;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository {
	List<City> getAllCities();
}
