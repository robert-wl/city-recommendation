package com.robert.codingchallenge.config;

import com.robert.codingchallenge.util.gramindex.impl.CityIndex;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FuzzyConfig {
	private final int Q_SIZE = 3;

	@Bean
	public CityIndex qGramIndex() {
		return new CityIndex(Q_SIZE);
	}

}
