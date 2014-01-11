package com.phatam.model;

public class CategoryWithNumberModel {
	private String text;
	private String number;
	
	public CategoryWithNumberModel(String name, String number)
	{
		text=name;
		this.number  = number;
	}
	public String getName()
	{
		return text;
	}
	
	public String getNumber()
	{
		return number;
	}

}
