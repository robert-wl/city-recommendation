package com.robert.codingchallenge.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CacheKeyGeneratorTest {

	@InjectMocks
	private CacheKeyGenerator cacheKeyGenerator;

	@Test
	void generate_WithSimpleParameters() throws NoSuchMethodException {
		Object target = new DummyService();
		Method method = DummyService.class.getMethod("dummyMethod", String.class, int.class);
		Object[] params = { "testParam", 42 };

		Object result = cacheKeyGenerator.generate(target, method, params);

		assertEquals("DummyService#dummyMethod#testParam#42", result);
	}

	@Test
	void generate_WithNullParameter() throws NoSuchMethodException {
		Object target = new DummyService();
		Method method = DummyService.class.getMethod("dummyMethod", String.class, int.class);
		Object[] params = { "testParam", null };

		Object result = cacheKeyGenerator.generate(target, method, params);

		assertEquals("DummyService#dummyMethod#testParam#", result);
	}

	@Test
	void generate_WithComplexObject() throws NoSuchMethodException {
		Object target = new DummyService();
		Method method = DummyService.class.getMethod("complexMethod", DummyObject.class);
		DummyObject dummyObject = new DummyObject("John", 30);
		Object[] params = { dummyObject };

		Object result = cacheKeyGenerator.generate(target, method, params);

		assertEquals("DummyService#complexMethod#John#30", result);
	}

	@Test
	void generate_WithEmptyParams() throws NoSuchMethodException {
		Object target = new DummyService();
		Method method = DummyService.class.getMethod("emptyParamsMethod");
		Object[] params = {};

		Object result = cacheKeyGenerator.generate(target, method, params);
		
		assertEquals("DummyService#emptyParamsMethod#", result);
	}


	static class DummyService {
		public void dummyMethod(String param1, int param2) {
		}

		public void complexMethod(DummyObject object) {
		}

		public void emptyParamsMethod() {
		}
	}


	static class DummyObject {
		private final String name;
		private final int age;

		DummyObject(String name, int age) {
			this.name = name;
			this.age = age;
		}
	}
}
