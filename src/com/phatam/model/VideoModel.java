package com.phatam.model;

import android.graphics.Bitmap;

public class VideoModel {
	private String name;
	private String author;
	private Bitmap image;
	
	
	public VideoModel(Bitmap image, String name, String author)
	{
		this.image = image;
		this.name=name;
		this.author = author;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name=name;
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author=author;
	}

}
