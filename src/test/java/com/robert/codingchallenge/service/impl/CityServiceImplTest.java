package com.robert.codingchallenge.service.impl;

import com.robert.codingchallenge.mapper.CityMapper;
import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.model.dto.ScoredCityDTO;
import com.robert.codingchallenge.model.dto.request.PaginationDTO;
import com.robert.codingchallenge.model.dto.request.SuggestionsRequestDTO;
import com.robert.codingchallenge.repository.CityRepository;
import com.robert.codingchallenge.util.fuzzysearch.SearchMatch;
import com.robert.codingchallenge.util.stringcomparator.StringAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CityServiceImplTest {
	@Mock
	private CityRepository cityRepository;
	@Mock
	private CityMapper cityMapper;

	@InjectMocks
	private CityServiceImpl cityService;


	@Test
	void testSearchCities() {
		String q = "London";
		SuggestionsRequestDTO dto = new SuggestionsRequestDTO(q, null, null, null, null, null);

		City city = Mockito.mock(City.class);

		List<SearchMatch<City>> searchMatches = List.of(new SearchMatch<>(city, 1.0));
		when(cityRepository.getCitiesByName(q)).thenReturn(searchMatches);

		ScoredCityDTO scoredCityDTO = ScoredCityDTO.builder()
				.name("London")
				.score(1.0)
				.build();

		when(cityMapper.toScoredCities(searchMatches)).thenReturn(List.of(scoredCityDTO));

		List<ScoredCityDTO> result = cityService.searchCities(dto);

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals("London", result.get(0).getName());
		assertEquals(1.0, result.get(0).getScore());
	}

	@Test
	void testSearchCitiesPaginated() {
		String q = "London";
		SuggestionsRequestDTO dto = new SuggestionsRequestDTO(q, null, null, null, null, null);

		City city = Mockito.mock(City.class);

		List<SearchMatch<City>> searchMatches = List.of(new SearchMatch<>(city, 1.0));
		when(cityRepository.getCitiesByName(q)).thenReturn(searchMatches);

		ScoredCityDTO scoredCityDTO = ScoredCityDTO.builder()
				.name("London")
				.score(1.0)
				.build();

		PaginationDTO paginationDTO = mock(PaginationDTO.class);
		when(paginationDTO.page()).thenReturn(1);
		when(paginationDTO.pageSize()).thenReturn(10);

		when(cityMapper.toScoredCities(searchMatches)).thenReturn(List.of(scoredCityDTO));

		List<ScoredCityDTO> result = cityService.searchCitiesPaginated(dto, paginationDTO);

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals("London", result.get(0).getName());
		assertEquals(1.0, result.get(0).getScore());
	}

	@Test
	void testSearchCitiesPaginated_InvalidPagination() {
		SuggestionsRequestDTO dto = mock(SuggestionsRequestDTO.class);
		PaginationDTO paginationDTO = mock(PaginationDTO.class);
		when(paginationDTO.page()).thenReturn(1);
		when(paginationDTO.pageSize()).thenReturn(null);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
				                                                  cityService.searchCitiesPaginated(dto, paginationDTO)
		                                                 );
		assertEquals("Both page and pageSize must be provided", exception.getMessage());
	}

	@Test
	void testSearchCitiesPaginated_withNoCitiesFound() {
		SuggestionsRequestDTO dto = mock(SuggestionsRequestDTO.class);
		PaginationDTO paginationDTO = mock(PaginationDTO.class);
		when(dto.q()).thenReturn("NonExistentCity");
		when(paginationDTO.page()).thenReturn(1);
		when(paginationDTO.pageSize()).thenReturn(10);
		when(cityRepository.getCitiesByName("NonExistentCity", StringAlgorithm.LEVENSHTEIN)).thenReturn(List.of());

		List<ScoredCityDTO> result = cityService.searchCitiesPaginated(dto, paginationDTO);

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}
}
