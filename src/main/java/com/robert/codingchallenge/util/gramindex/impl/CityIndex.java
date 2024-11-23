package com.robert.codingchallenge.util.gramindex.impl;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.util.gramindex.QGramIndex;

import java.util.*;
import java.util.stream.Collectors;


public class CityIndex extends QGramIndex<City> {

	public CityIndex(int qSize) {
		super(qSize);
	}


	@Override
	public void add(City data) {
		List<String> words = new ArrayList<>();

		Optional.ofNullable(data.getAltNames()).ifPresent(words::addAll);
		Optional.ofNullable(data.getName()).ifPresent(words::add);

		Set<String> qGrams = words.stream()
				.map(String::toLowerCase)
				.flatMap(w -> generateQGram(w).stream())
				.collect(Collectors.toSet());

		qGrams.forEach(q -> index.computeIfAbsent(q, k -> new HashSet<>()).add(data));
	}

	@Override
	public Set<City> search(String q) {
		if (q.length() < qSize) {
			var temp = index.keySet().stream().toList();
			var res = temp.stream()
					.filter(k -> k.contains(q.toLowerCase()))
					.flatMap(k -> index.get(k).stream())
					.collect(Collectors.toSet());
			return index.keySet().stream()
					.filter(k -> k.contains(q.toLowerCase()))
					.flatMap(k -> index.get(k).stream())
					.collect(Collectors.toSet());
		}

		Set<String> qGrams = generateQGram(q.toLowerCase());
		return qGrams.parallelStream()
				.flatMap(qGram -> index.getOrDefault(qGram, Collections.emptySet()).stream())
				.collect(Collectors.toSet());
	}
}
