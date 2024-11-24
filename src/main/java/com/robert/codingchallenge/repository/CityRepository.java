package com.robert.codingchallenge.repository;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.util.fuzzysearch.SearchMatch;
import com.robert.codingchallenge.util.stringcomparator.StringAlgorithm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository {
	List<SearchMatch<City>> getCitiesByName(String q);

	List<SearchMatch<City>> getCitiesByName(String q, StringAlgorithm algo);
}
