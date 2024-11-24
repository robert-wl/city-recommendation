package com.robert.codingchallenge.util.stringcomparator;

public enum StringAlgorithm {
	LEVENSHTEIN("Levenshtein"),
	JARO_WINKLER("Jaro-Winkler"),
	JACCARD("Jaccard");

	private final String name;

	StringAlgorithm(String name) {
		this.name = name;
	}

	public static StringAlgorithm of(Integer value) {
		if (value == null) {
			return null;
		}
		return switch (value) {
			case 1 -> JARO_WINKLER;
			case 2 -> JACCARD;
			default -> LEVENSHTEIN;
		};
	}

	@Override
	public String toString() {
		return name;
	}
}
