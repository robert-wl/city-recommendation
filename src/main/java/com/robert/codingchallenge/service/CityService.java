package com.robert.codingchallenge.service;

import com.robert.codingchallenge.model.dto.ScoredCityDTO;
import com.robert.codingchallenge.model.dto.request.PaginationDTO;
import com.robert.codingchallenge.model.dto.request.SuggestionsRequestDTO;

import java.util.List;

public interface CityService {

	List<ScoredCityDTO> searchCities(SuggestionsRequestDTO dto);

	List<ScoredCityDTO> searchCitiesPaginated(SuggestionsRequestDTO dto, PaginationDTO paginationDTO);
}
