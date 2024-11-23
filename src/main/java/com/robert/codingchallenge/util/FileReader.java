package com.robert.codingchallenge.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@Component
public class FileReader {
	public String read(String path) {
		try {
			StringBuilder data = new StringBuilder();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);

			if (inputStream == null) {
				return "";
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			String line;

			while ((line = reader.readLine()) != null) {
				data.append(line).append("\n");
			}

			return data.toString();
		} catch (IOException e) {
			log.error("Error reading file: {}", e.getMessage());
			return "";
		}
	}
}
