package com.robert.codingchallenge.util;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class FileReaderTest {
	@InjectMocks
	private FileReader fileReader;


	@Test
	void testRead_validTSV_shouldReturnContent() {

		Optional<String> result = fileReader.read("test.txt");

		assertTrue(result.isPresent(), "FileReader should return content for valid file");
	}

	@Test
	void testRead_invalidTSV_shouldReturnEmpty() {
		Optional<String> data = fileReader.read("test-invalid.txt");

		assertFalse(data.isPresent(), "FileReader should return empty Optional for invalid file");
	}
}
