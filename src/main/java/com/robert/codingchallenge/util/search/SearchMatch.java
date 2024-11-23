package com.robert.codingchallenge.util.search;

public record SearchMatch<T>(
		T data,
		Double score
) {
}
