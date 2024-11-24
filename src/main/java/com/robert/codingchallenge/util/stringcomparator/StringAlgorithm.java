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
		switch (value) {
			case 0:
				return LEVENSHTEIN;
			case 1:
				return JARO_WINKLER;
			case 2:
				return JACCARD;
			default:
				return null;
		}
	}

	@Override
	public String toString() {
		return name;
	}
}
