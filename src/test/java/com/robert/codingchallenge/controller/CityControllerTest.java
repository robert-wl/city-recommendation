package com.robert.codingchallenge.controller;

import com.robert.codingchallenge.config.RateLimitConfig;
import com.robert.codingchallenge.model.dto.ScoredCityDTO;
import com.robert.codingchallenge.model.dto.request.PaginationDTO;
import com.robert.codingchallenge.model.dto.request.SuggestionsRequestDTO;
import com.robert.codingchallenge.service.CityService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CityController.class)
@Import(RateLimitConfig.class)
public class CityControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private CityService cityService;

	@Test
	void testGetSuggestions_withValidParameters() throws Exception {

		ScoredCityDTO mockCity1 = mock(ScoredCityDTO.class);
		when(mockCity1.getName()).thenReturn("City1");
		ScoredCityDTO mockCity2 = mock(ScoredCityDTO.class);
		when(mockCity2.getName()).thenReturn("City2");
		ScoredCityDTO mockCity3 = mock(ScoredCityDTO.class);
		when(mockCity3.getName()).thenReturn("City3");

		List<ScoredCityDTO> cityList = List.of(mockCity1, mockCity2, mockCity3);


		when(cityService.searchCitiesPaginated(any(SuggestionsRequestDTO.class), any(PaginationDTO.class)))
				.thenReturn(cityList);


		mockMvc.perform(get("/v1/suggestions")
				                .param("q", "testQuery")
				                .param("latitude", "45.0")
				                .param("longitude", "90.0")
				                .param("page", "1")
				                .param("pageSize", "10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.suggestions").isArray())
				.andExpect(jsonPath("$.suggestions.length()").value(3))
				.andExpect(jsonPath("$.suggestions[0].name").value("City1"))
				.andExpect(jsonPath("$.suggestions[1].name").value("City2"))
				.andExpect(jsonPath("$.suggestions[2].name").value("City3"));
	}

	@Test
	void testGetSuggestions_withInvalidLatitude() throws Exception {
		mockMvc.perform(get("/v1/suggestions")
				                .param("q", "testQuery")
				                .param("latitude", "100.0")
				                .param("longitude", "90.0")
				                .param("page", "1")
				                .param("pageSize", "10"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message.latitude", Matchers.containsString("Latitude must be between")));
	}

	@Test
	void testGetSuggestions_withInvalidLongitude() throws Exception {
		mockMvc.perform(get("/v1/suggestions")
				                .param("q", "testQuery")
				                .param("latitude", "45.0")
				                .param("longitude", "200.0")
				                .param("page", "1")
				                .param("pageSize", "10"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message.longitude", Matchers.containsString("Longitude must be between")));
	}

	@Test
	void testGetSuggestions_withMissingLatitudeAndLongitude() throws Exception {

		ScoredCityDTO mockCity1 = mock(ScoredCityDTO.class);
		when(mockCity1.getName()).thenReturn("City1");
		ScoredCityDTO mockCity2 = mock(ScoredCityDTO.class);
		when(mockCity2.getName()).thenReturn("City2");

		List<ScoredCityDTO> cityList = List.of(mockCity1, mockCity2);


		when(cityService.searchCitiesPaginated(any(SuggestionsRequestDTO.class), any(PaginationDTO.class)))
				.thenReturn(cityList);


		mockMvc.perform(get("/v1/suggestions")
				                .param("q", "testQuery")
				                .param("page", "1")
				                .param("pageSize", "10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.suggestions").isArray())
				.andExpect(jsonPath("$.suggestions.length()").value(2))
				.andExpect(jsonPath("$.suggestions[0].name").value("City1"))
				.andExpect(jsonPath("$.suggestions[1].name").value("City2"));
	}

	@Test
	void testGetSuggestions_withInvalidPagination() throws Exception {
		mockMvc.perform(get("/v1/suggestions")
				                .param("q", "testQuery")
				                .param("latitude", "45.0")
				                .param("longitude", "90.0")
				                .param("page", "1")
				                .param("pageSize", "1000"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message.pageSize", Matchers.containsString("Page size")));
	}
}
