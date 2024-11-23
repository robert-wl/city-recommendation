package com.robert.codingchallenge.util.stringcomparator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StringComparatorTest {
	@InjectMocks
	private StringComparator stringComparator;

	@Test
	void testCompareLevenshtein_sameStrings() {
		String s1 = "hello";
		String s2 = "hello";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.LEVENSHTEIN);

		Assertions.assertEquals(1.0, score, 0.0001);
	}

	@Test
	void testCompareLevenshtein_differentStrings() {
		String s1 = "hello";
		String s2 = "world";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.LEVENSHTEIN);

		Assertions.assertTrue(score < 1.0);
	}

	@Test
	void testCompareLevenshtein_prefixMatch() {
		String s1 = "hello";
		String s2 = "he";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.LEVENSHTEIN);

		Assertions.assertTrue(score > 0.4);
	}

	@Test
	void testCompareJaroWinkler_sameStrings() {
		String s1 = "hello";
		String s2 = "hello";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.JARO_WINKLER);

		Assertions.assertEquals(1.0, score, 0.0001);
	}

	@Test
	void testCompareJaroWinkler_differentStrings() {
		String s1 = "hello";
		String s2 = "world";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.JARO_WINKLER);

		Assertions.assertTrue(score < 1.0);
	}

	@Test
	void testCompareJaroWinkler_prefixMatch() {
		String s1 = "hello";
		String s2 = "he";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.JARO_WINKLER);

		Assertions.assertTrue(score > 0.4);
	}

	@Test
	void testCompareJaccard_sameStrings() {
		String s1 = "hello";
		String s2 = "hello";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.JACCARD);

		Assertions.assertEquals(1.0, score, 0.0001);
	}

	@Test
	void testCompareJaccard_differentStrings() {
		String s1 = "hello";
		String s2 = "world";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.JACCARD);

		Assertions.assertTrue(score < 1.0);
	}

	@Test
	void testCompareJaccard_prefixMatch() {
		String s1 = "hello";
		String s2 = "he";

		double score = stringComparator.compare(s1, s2, StringAlgorithm.JACCARD);

		Assertions.assertTrue(score > 0.4);
	}
}
