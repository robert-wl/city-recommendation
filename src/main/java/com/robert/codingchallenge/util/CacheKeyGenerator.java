package com.robert.codingchallenge.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CacheKeyGenerator {
	public String generate(Object... objects) {
		StringBuilder key = new StringBuilder();

		return Arrays.stream(objects)
				.map(this::extractFields)
				.collect(Collectors.joining("#"));
	}

	private String extractFields(Object object) {
		if (object == null) {
			return "";
		}

		Class<?> clazz = object.getClass();
		StringBuilder result = new StringBuilder();

		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				var value = Optional.ofNullable(field.get(object)).orElse("");
				result.append(value).append("#");
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Failed to access field " + field.getName(), e);
			}
		}

		return result.substring(0, result.length() - 1);
	}
}
