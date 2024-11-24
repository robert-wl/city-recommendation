package com.robert.codingchallenge.controller;

import com.robert.codingchallenge.model.dto.ScoredCityDTO;
import com.robert.codingchallenge.model.dto.request.PaginationDTO;
import com.robert.codingchallenge.model.dto.request.SuggestionsRequestDTO;
import com.robert.codingchallenge.service.CityService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(controllers = CityController.class)
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


		mockMvc.perform(MockMvcRequestBuilders.get("/v1/suggestions")
				                .param("q", "testQuery")
				                .param("latitude", "45.0")
				                .param("longitude", "90.0")
				                .param("page", "1")
				                .param("pageSize", "10"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.suggestions").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.suggestions.length()").value(3))
				.andExpect(MockMvcResultMatchers.jsonPath("$.suggestions[0].name").value("City1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.suggestions[1].name").value("City2"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.suggestions[2].name").value("City3"));
	}

	@Test
	void testGetSuggestions_withInvalidLatitude() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/suggestions")
				                .param("q", "testQuery")
				                .param("latitude", "100.0")
				                .param("longitude", "90.0")
				                .param("page", "1")
				                .param("pageSize", "10"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message.latitude", Matchers.containsString("Latitude must be between")));
	}

	@Test
	void testGetSuggestions_withInvalidLongitude() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/suggestions")
				                .param("q", "testQuery")
				                .param("latitude", "45.0")
				                .param("longitude", "200.0")
				                .param("page", "1")
				                .param("pageSize", "10"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message.longitude", Matchers.containsString("Longitude must be between")));
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


		mockMvc.perform(MockMvcRequestBuilders.get("/v1/suggestions")
				                .param("q", "testQuery")
				                .param("page", "1")
				                .param("pageSize", "10"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.suggestions").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.suggestions.length()").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$.suggestions[0].name").value("City1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.suggestions[1].name").value("City2"));
	}

	@Test
	void testGetSuggestions_withInvalidPagination() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/suggestions")
				                .param("q", "testQuery")
				                .param("latitude", "45.0")
				                .param("longitude", "90.0")
				                .param("page", "1")
				                .param("pageSize", "1000"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message.pageSize", Matchers.containsString("Page size")));
	}
}
