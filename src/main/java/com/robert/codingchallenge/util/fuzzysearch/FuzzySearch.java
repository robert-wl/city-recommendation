package com.robert.codingchallenge.util.fuzzysearch;

import com.robert.codingchallenge.util.stringcomparator.StringAlgorithm;

import java.util.List;

public interface FuzzySearch<T> {

	void add(T data);

	List<SearchMatch<T>> search(String query);

	List<SearchMatch<T>> search(String query, StringAlgorithm algo);
}
