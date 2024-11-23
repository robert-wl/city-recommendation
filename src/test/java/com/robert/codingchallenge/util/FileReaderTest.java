package com.robert.codingchallenge.util;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FileReaderTest {
	@InjectMocks
	private FileReader fileReader;


	@Test
	void testRead_validTSV_shouldReturnContent() {

		Optional<String> result = fileReader.read("test.txt");

		Assertions.assertTrue(result.isPresent());
	}

	@Test
	void testRead_invalidTSV_shouldReturnEmpty() {
		Optional<String> data = fileReader.read("test-invalid.txt");

		Assertions.assertFalse(data.isPresent());
	}
}
