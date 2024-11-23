package com.robert.codingchallenge.util;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GeoCalculatorTest {
	@InjectMocks
	private GeoCalculator geoCalculator;

	@Test
	void testScore_maxScore() {
		double latitude = 51.5074;
		double longitude = -0.1278;

		double score = geoCalculator.score(latitude, longitude, latitude, longitude);

		Assertions.assertEquals(1.0, score, 0.0001);
	}

	@Test
	void testScore_halfScore() {
		double latitude1 = 51.5074;
		double longitude1 = -0.1278;

		double latitude2 = 53.3498;
		double longitude2 = -6.2603;

		double score = geoCalculator.score(latitude1, longitude1, latitude2, longitude2);

		Assertions.assertTrue(score > 0.4 && score < 0.6, "Score should be around 0.5");
	}

	@Test
	void testScore_minScore() {
		double latitude1 = 51.5074;
		double longitude1 = -0.1278;

		double latitude2 = -33.8688;
		double longitude2 = 151.2093;

		double score = geoCalculator.score(latitude1, longitude1, latitude2, longitude2);

		Assertions.assertEquals(0.0, score, 0.0001);
	}
}
