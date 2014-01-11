package com.phatam.model;

public class AuthorModel {
	private String text;

	public AuthorModel(String name) {
		text = name;
	}

	public String getName() {
		return text;
	}

}
