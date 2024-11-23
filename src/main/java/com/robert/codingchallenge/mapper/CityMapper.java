package com.robert.codingchallenge.mapper;

import com.robert.codingchallenge.model.data.City;
import com.robert.codingchallenge.model.dto.ScoredCityDTO;
import com.robert.codingchallenge.util.search.SearchMatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {

	@Mappings({
			@Mapping(target = "name", expression = "java(getNameField(city))"),
			@Mapping(target = "latitude", expression = "java(city.getData().getLatitude())"),
			@Mapping(target = "longitude", expression = "java(city.getData().getLongitude())"),
			@Mapping(target = "score", expression = "java(city.getScore())"),

	})
	ScoredCityDTO toScoredCity(SearchMatch<City> city);

	List<ScoredCityDTO> toScoredCities(List<SearchMatch<City>> cities);

	@Named("toFormattedName")
	default String getNameField(SearchMatch<City> citySearchMatch) {
		City city = citySearchMatch.getData();
		return String.format("%s, %s", city.getName(), city.getCountry());
	}
}
