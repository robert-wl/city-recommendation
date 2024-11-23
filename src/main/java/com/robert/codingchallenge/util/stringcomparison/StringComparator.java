package com.robert.codingchallenge.util.stringcomparison;


import org.apache.commons.text.similarity.JaccardDistance;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

@Component
public class StringComparator {
	private final double SCORE_RATIO = 0.6;

	public double compare(String s1, String s2, StringAlgorithm algorithm) {
		return switch (algorithm) {
			case LEVENSHTEIN -> compareLevenshtein(s1, s2);
			case JARO_WINKLER -> compareJaroWinkler(s1, s2);
			case JACCARD -> compareJaccard(s1, s2);
		};
	}

	private double compareLevenshtein(String s, String q) {
		LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
		int distance = levenshteinDistance.apply(s, q);
		int maxLength = Math.max(s.length(), q.length());

		double baseScore = (1 - (double) distance / maxLength) * SCORE_RATIO;

		if (s.toLowerCase().startsWith(q.toLowerCase())) {
			return baseScore + (1 - SCORE_RATIO);
		}

		return baseScore;
	}

	private double compareJaroWinkler(String s, String q) {
		JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();
		double baseScore = jaroWinklerDistance.apply(s, q) * SCORE_RATIO;


		if (s.toLowerCase().startsWith(q.toLowerCase())) {
			return baseScore + (1 - SCORE_RATIO);
		}

		return baseScore;
	}

	private double compareJaccard(String s, String q) {
		JaccardDistance jaccardDistance = new JaccardDistance();
		double baseScore = (1 - jaccardDistance.apply(s, q)) * SCORE_RATIO;

		if (s.toLowerCase().startsWith(q.toLowerCase())) {
			return baseScore + (1 - SCORE_RATIO);
		}

		return baseScore;
	}
}
