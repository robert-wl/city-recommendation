package com.robert.codingchallenge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public ModelResolver modelResolver(ObjectMapper objectMapper) {
		return new ModelResolver(objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));
	}

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
				.title("City Suggestions API")
				.version("1.0")
				.description("City Suggestions API provides endpoint that suggests cities based on queries.");

		return new OpenAPI()
				.info(info);
	}

}
