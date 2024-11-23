package com.robert.codingchallenge.util;

import org.springframework.stereotype.Component;

@Component
public class GeoCalculator {

	private static final double EARTH_RADIUS_KM = 6371.0;
	private static final double MAX_DISTANCE_KM = 1000.0;

	private double haversine(double lon1, double lat1, double lon2, double lat2) {
		lon1 = Math.toRadians(lon1);
		lat1 = Math.toRadians(lat1);
		lon2 = Math.toRadians(lon2);
		lat2 = Math.toRadians(lat2);

		double dLat = lat2 - lat1;
		double dLon = lon2 - lon1;

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
				Math.cos(lat1) * Math.cos(lat2) *
						Math.sin(dLon / 2) * Math.sin(dLon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS_KM * c;
	}

	public double score(
			double latitude1, double longitude1,
			double latitude2, double longitude2) {
		double distance = haversine(longitude1, latitude1, longitude2, latitude2);

		return Math.max(0, 1 - distance / MAX_DISTANCE_KM);
	}
}
