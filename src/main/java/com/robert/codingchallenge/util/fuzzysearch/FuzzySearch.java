package com.robert.codingchallenge.util.fuzzysearch;

import java.util.List;

public interface FuzzySearch<T> {

	void add(T data);

	List<SearchMatch<T>> search(String query);
}
