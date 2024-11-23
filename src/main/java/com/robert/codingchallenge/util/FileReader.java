package com.robert.codingchallenge.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

@Slf4j
@Component
public class FileReader {
	public Optional<String> read(String path) {
		try (
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
				BufferedReader reader = inputStream == null ? null : new BufferedReader(new InputStreamReader(inputStream))
		) {
			StringBuilder data = new StringBuilder();

			if (reader == null) {
				return Optional.empty();
			}

			String line;

			while ((line = reader.readLine()) != null) {
				data.append(line).append("\n");
			}

			return Optional.of(data.toString());
		} catch (IOException e) {
			log.error("Error reading file: {}", e.getMessage());
			return Optional.empty();
		}
	}
}
