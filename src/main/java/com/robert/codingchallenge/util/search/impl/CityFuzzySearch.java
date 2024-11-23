package com.robert.codingchallenge.util.search.impl;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.util.gramindex.impl.CityIndex;
import com.robert.codingchallenge.util.search.FuzzySearch;
import com.robert.codingchallenge.util.search.SearchMatch;
import lombok.AllArgsConstructor;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class CityFuzzySearch implements FuzzySearch<City> {
	private final CityIndex index;
	private final JaroWinklerDistance distance;

	@Override
	public void add(City data) {
		index.add(data);
	}

	@Override
	public List<SearchMatch<City>> search(String query) {
		Set<City> result = index.search(query);

		return result.stream()
				.map(c -> new SearchMatch<>(c, 1 - distance.apply(c.getName(), query)))
				.filter(m -> m.score() > 0.5)
				.sorted((m1, m2) -> m2.score().compareTo(m1.score()))
				.collect(Collectors.toList());
	}

}
