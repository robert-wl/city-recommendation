package com.robert.codingchallenge.util;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("cacheKeyGenerator")
public class CacheKeyGenerator implements KeyGenerator {

	@NonNull
	@Override
	public Object generate(Object target, Method method, @NonNull Object... params) {
		return target.getClass().getSimpleName() + "#"
				+ method.getName() + "#"
				+ Arrays.stream(params)
				.map(this::extractFields)
				.collect(Collectors.joining("#"));
	}


	private String extractFields(Object object) {
		if (object == null) {
			return "";
		}

		Class<?> clazz = object.getClass();

		if (clazz.getPackageName().startsWith("java.")) {
			return object.toString();
		}

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
