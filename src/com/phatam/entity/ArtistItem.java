package com.phatam.entity;

public class ArtistItem {
	String artist;
	int cnt;

	public ArtistItem(String artist, int cnt) {
		this.artist = artist;
		this.cnt = cnt;
	}

	public String getArist() {
		return this.artist;
	}

	public int getCnt() {
		return this.cnt;
	}
}
