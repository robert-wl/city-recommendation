package com.robert.codingchallenge.util.gramindex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class QGramIndex<T> {
	protected final Map<String, Set<T>> index = new HashMap<>();
	protected int qSize;

	public QGramIndex(int qSize) {
		this.qSize = qSize;
	}

	protected Set<String> generateQGram(String s) {
		Set<String> qGrams = new HashSet<>();

		for (int i = 0; i < s.length() - qSize + 1; i++) {
			qGrams.add(s.substring(i, i + qSize));
		}

		return qGrams;
	}

	public abstract void add(T data);

	public abstract Set<T> search(String T);

}
