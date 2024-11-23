package com.robert.codingchallenge.util.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchMatch<T> {
	T data;
	Double score;
}
