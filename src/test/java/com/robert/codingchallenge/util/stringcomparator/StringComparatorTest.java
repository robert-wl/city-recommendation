package com.robert.codingchallenge.util.stringcomparator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class StringComparatorTest {
	@InjectMocks
	private StringComparator stringComparator;

	@Test
	void testCompareLevenshtein_sameStrings() {
		String s1 = "hello";
		String s2 = "hello";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.LEVENSHTEIN);

		assertEquals(1.0, score, 0.0001, "Score should be 1.0");
	}

	@Test
	void testCompareLevenshtein_differentStrings() {
		String s1 = "hello";
		String s2 = "world";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.LEVENSHTEIN);

		assertTrue(score < 1.0, "Score should be less than 1.0");
	}

	@Test
	void testCompareLevenshtein_prefixMatch() {
		String s1 = "hello";
		String s2 = "he";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.LEVENSHTEIN);

		assertTrue(score > 0.4, "Score should be greater than 0.4");
	}

	@Test
	void testCompareJaroWinkler_sameStrings() {
		String s1 = "hello";
		String s2 = "hello";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.JARO_WINKLER);

		assertEquals(1.0, score, 0.0001, "Score should be 1.0");
	}

	@Test
	void testCompareJaroWinkler_differentStrings() {
		String s1 = "hello";
		String s2 = "world";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.JARO_WINKLER);

		assertTrue(score < 1.0, "Score should be less than 1.0");
	}

	@Test
	void testCompareJaroWinkler_prefixMatch() {
		String s1 = "hello";
		String s2 = "he";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.JARO_WINKLER);

		assertTrue(score > 0.4, "Score should be greater than 0.4");
	}

	@Test
	void testCompareJaccard_sameStrings() {
		String s1 = "hello";
		String s2 = "hello";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.JACCARD);

		assertEquals(1.0, score, 0.0001, "Score should be 1.0");
	}

	@Test
	void testCompareJaccard_differentStrings() {
		String s1 = "hello";
		String s2 = "world";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.JACCARD);

		assertTrue(score < 1.0, "Score should be less than 1.0");
	}

	@Test
	void testCompareJaccard_prefixMatch() {
		String s1 = "hello";
		String s2 = "he";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.JACCARD);

		assertTrue(score > 0.4, "Score should be greater than 0.4");
	}
}
