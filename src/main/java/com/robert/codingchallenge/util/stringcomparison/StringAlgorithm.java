package com.robert.codingchallenge.util.stringcomparison;

public enum StringAlgorithm {
	LEVENSHTEIN("Levenshtein"),
	JARO_WINKLER("Jaro-Winkler"),
	JACCARD("Jaccard");

	private final String name;

	StringAlgorithm(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
