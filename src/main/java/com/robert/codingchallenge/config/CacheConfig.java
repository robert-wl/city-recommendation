package com.robert.codingchallenge.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {
	
	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
		Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
				.expireAfterWrite(10, TimeUnit.MINUTES)
				.recordStats();
		caffeineCacheManager.setCaffeine(caffeine);

		return caffeineCacheManager;
	}
}
