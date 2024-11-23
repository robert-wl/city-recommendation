package com.robert.codingchallenge.util.search.impl;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.util.gramindex.impl.CityIndex;
import com.robert.codingchallenge.util.search.FuzzySearch;
import com.robert.codingchallenge.util.search.SearchMatch;
import com.robert.codingchallenge.util.stringcomparison.StringAlgorithm;
import com.robert.codingchallenge.util.stringcomparison.StringComparator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class CityFuzzySearch implements FuzzySearch<City> {
	private final double SCORE_THRESHOLD = 0.5;
	private final CityIndex index;
	private final StringComparator comparator;

	@Override
	public void add(City data) {
		index.add(data);
	}

	@Override
	public List<SearchMatch<City>> search(String query) {
		Set<City> result = index.search(query);

		return result.stream()
				.map(c -> new SearchMatch<>(c, comparator.compare(c.getName().toLowerCase(), query.toLowerCase(), StringAlgorithm.JACCARD)))
				.filter(m -> m.getScore() >= SCORE_THRESHOLD)
				.peek(m -> m.setScore((m.getScore() - SCORE_THRESHOLD) / SCORE_THRESHOLD))
				.filter(m -> m.getScore() != 0)
				.sorted((m1, m2) -> m2.getScore().compareTo(m1.getScore()))
				.collect(Collectors.toList());
	}

}
