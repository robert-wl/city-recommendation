package com.robert.codingchallenge.util.fuzzysearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchMatch<T> {
	private T data;
	private Double score;
}
