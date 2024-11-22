package com.robert.codingchallenge.util.stringcomparison;


import org.apache.commons.text.similarity.JaccardDistance;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

@Component
public class StringComparator {

	public double compare(String s1, String s2, StringAlgorithm algorithm) {
		return switch (algorithm) {
			case LEVENSHTEIN -> compareLevenshtein(s1, s2);
			case JARO_WINKLER -> compareJaroWinkler(s1, s2);
			case JACCARD -> compareJaccard(s1, s2);
		};
	}

	private double compareLevenshtein(String s1, String s2) {
		LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
		return levenshteinDistance.apply(s1, s2);
	}

	private double compareJaroWinkler(String s1, String s2) {
		JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();
		return jaroWinklerDistance.apply(s1, s2);
	}

	private double compareJaccard(String s1, String s2) {
		JaccardDistance jaccardDistance = new JaccardDistance();
		return jaccardDistance.apply(s1, s2);
	}
}
