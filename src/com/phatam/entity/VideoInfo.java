package com.phatam.entity;

public class VideoInfo {
	protected int id;
	protected int site_views;
	protected String uniq_id;
	protected String artist;
	protected String video_title;
	protected String description;
	protected String yt_id;
	protected String yt_thumb;
	protected String mp3;
	protected String category;

	public int getId() {
		return this.id;
	}
	
	public String getCategory() {
		return this.category;
	}

	public String getUniqueId() {
		return this.uniq_id;
	}

	public String getArtist() {
		return this.artist;
	}

	public String getVideoTitle() {
		return this.video_title;
	}

	public String getDescription() {
		return this.description;
	}

	public String getYoutubeId() {
		return this.yt_id;
	}

	public String getYoutubeImage() {
		return this.yt_thumb;
	}

	public int getSiteView() {
		return this.site_views;
	}

	public String getMp3() {
		return this.mp3;
	}
	public void setVideoId(String id){
		this.uniq_id = id;
	}
	public void setArtist(String artist){
		this.artist = artist;
	}
	public void setVideoTitle(String title){
		this.video_title = title;
	}
	public void setDesc(String desc){
		this.description = desc;
	}
	public void setYoutubeId(String id){
		this.yt_id = id;
	}
	public void setYoutubeImage(String img){
		this.yt_thumb = img;
	}
	public void setSiteView(int site_views){
		this.site_views = site_views;
	}
	public void setMp3(String mp3){
		this.mp3 = mp3;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
}
