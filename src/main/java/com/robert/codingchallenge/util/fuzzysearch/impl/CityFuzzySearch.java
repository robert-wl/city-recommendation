package com.robert.codingchallenge.util.fuzzysearch.impl;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.util.fuzzysearch.FuzzySearch;
import com.robert.codingchallenge.util.fuzzysearch.SearchMatch;
import com.robert.codingchallenge.util.gramindex.impl.CityIndex;
import com.robert.codingchallenge.util.stringcomparator.StringAlgorithm;
import com.robert.codingchallenge.util.stringcomparator.StringComparator;
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

	private SearchMatch<City> createSearchMatch(City city, String queryLower) {
		double score = comparator.compare(city.getName().toLowerCase(), queryLower, StringAlgorithm.JACCARD);
		return new SearchMatch<>(city, score);
	}

	private void normalizeScore(SearchMatch<City> match) {
		double normalizedScore = (match.getScore() - SCORE_THRESHOLD) / SCORE_THRESHOLD;
		match.setScore(normalizedScore);
	}

	@Override
	public List<SearchMatch<City>> search(String query) {
		Set<City> result = index.search(query);

		if (result.isEmpty()) {
			return List.of();
		}

		String queryLower = query.toLowerCase();

		return result.stream()
				.map(c -> createSearchMatch(c, queryLower))
				.filter(m -> m.getScore() >= SCORE_THRESHOLD)
				.peek(this::normalizeScore)
				.filter(m -> m.getScore() != 0)
				.sorted((m1, m2) -> Double.compare(m2.getScore(), m1.getScore()))
				.collect(Collectors.toList());
	}

}
