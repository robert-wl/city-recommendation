package com.robert.codingchallenge.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Component
public class TSVParser {

	private static final Map<Class<?>, Function<String, ?>> PARSERS = Map.of(
			String.class, Function.identity(),
			Integer.class, Integer::parseInt,
			Float.class, Float::parseFloat,
			Double.class, Double::parseDouble,
			Boolean.class, Boolean::parseBoolean,
			List.class, value -> List.of(value.split(","))
	                                                                        );

	private Map<Integer, String> getIndexToHeader(String header) {
		Map<Integer, String> indexToHeader = new HashMap<>();
		String[] headers = header.split("\t");

		for (int i = 0; i < headers.length; i++) {
			indexToHeader.put(i, headers[i]);
		}

		return indexToHeader;
	}

	private <T> T castValue(Class<T> type, String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}

		var parser = Optional.ofNullable(PARSERS.get(type))
				.orElseThrow(() -> new IllegalArgumentException("Unsupported type: " + type));
		

		return type.cast(parser.apply(value));
	}

	public <T> List<T> parse(String tsv, Map<String, String> headerToField, Class<T> clazz) {
		List<T> list = new ArrayList<>();

		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(tsv);
		     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
		) {
			String line = reader.readLine();

			Map<Integer, String> indexToHeader = getIndexToHeader(line);

			List<Integer> takeIndex = indexToHeader.keySet().stream()
					.filter(index -> headerToField.containsKey(indexToHeader.get(index)))
					.toList();

			while ((line = reader.readLine()) != null) {
				String[] values = line.split("\t");
				T obj = clazz.getDeclaredConstructor().newInstance();

				for (int index : takeIndex) {
					String header = indexToHeader.get(index);
					String field = headerToField.get(header);

					String setter = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
					Class<?> type = clazz.getDeclaredField(field).getType();
					Method method = clazz.getMethod(setter, type);

					method.invoke(obj, castValue(type, values[index]));
				}

				list.add(obj);
			}

			return list;
		} catch (Exception e) {
			log.error("Error parsing TSV file", e);
			return new ArrayList<>();
		}
	}
}
