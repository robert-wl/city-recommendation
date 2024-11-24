package com.robert.codingchallenge.util.fuzzysearch.impl;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.util.fuzzysearch.FuzzySearch;
import com.robert.codingchallenge.util.fuzzysearch.SearchMatch;
import com.robert.codingchallenge.util.gramindex.impl.CityIndex;
import com.robert.codingchallenge.util.stringcomparator.StringAlgorithm;
import com.robert.codingchallenge.util.stringcomparator.StringComparator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

	private SearchMatch<City> createSearchMatch(City city, String queryLower, StringAlgorithm algorithm) {
		double score = comparator.compare(city.getName().toLowerCase(), queryLower, algorithm);
		return new SearchMatch<>(city, score);
	}

	private void normalizeScore(SearchMatch<City> match) {
		double normalizedScore = (match.getScore() - SCORE_THRESHOLD) / SCORE_THRESHOLD;
		match.setScore(normalizedScore);
	}

	private List<SearchMatch<City>> processSearchResults(Set<City> result, String queryLower, StringAlgorithm algorithm) {
		if (result.isEmpty()) {
			return Collections.emptyList();
		}

		return result.stream()
				.map(city -> createSearchMatch(city, queryLower, algorithm))
				.filter(match -> match.getScore() >= SCORE_THRESHOLD)
				.peek(this::normalizeScore)
				.filter(match -> match.getScore() != 0)
				.sorted((match1, match2) -> Double.compare(match2.getScore(), match1.getScore()))
				.collect(Collectors.toList());
	}

	@Override
	public List<SearchMatch<City>> search(String query) {
		return search(query, StringAlgorithm.JACCARD);
	}

	@Override
	public List<SearchMatch<City>> search(String query, StringAlgorithm algorithm) {
		String queryLower = Optional.ofNullable(query).orElse("").toLowerCase();
		Set<City> result = index.search(queryLower);
		return processSearchResults(result, queryLower, algorithm);
	}
}
