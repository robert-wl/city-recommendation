package com.robert.codingchallenge.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CacheKeyGeneratorTest {

	@InjectMocks
	private CacheKeyGenerator cacheKeyGenerator;

	@Test
	void testGenerate_withSingleObject() {
		TestObject testObject = new TestObject("value1", 42, null);
		String result = cacheKeyGenerator.generate(testObject);

		assertEquals("value1#42#", result, "Cache key generation failed for a single object");
	}

	@Test
	void testGenerate_withMultipleObjects() {
		TestObject testObject1 = new TestObject("value1", 42, "extra");
		TestObject testObject2 = new TestObject("another", null, "final");
		String result = cacheKeyGenerator.generate(testObject1, testObject2);

		assertEquals("value1#42#extra#another##final", result, "Cache key generation failed for multiple objects");
	}

	@Test
	void testGenerate_withNullObject() {
		String result = cacheKeyGenerator.generate((Object) null);

		assertEquals("", result, "Cache key generation failed for a null object");
	}

	@Test
	void testGenerate_withEmptyFields() {
		TestObject emptyObject = new TestObject(null, null, null);
		String result = cacheKeyGenerator.generate(emptyObject);

		assertEquals("##", result, "Cache key generation failed for an object with empty fields");
	}

	@Test
	void testGenerate_withNoObjects() {
		String result = cacheKeyGenerator.generate();

		assertEquals("", result, "Cache key generation failed for no objects");
	}


	private static class TestObject {
		private final String field1;
		private final Integer field2;
		private final String field3;

		public TestObject(String field1, Integer field2, String field3) {
			this.field1 = field1;
			this.field2 = field2;
			this.field3 = field3;
		}
	}
}
