package com.phatam.model;

import org.json.JSONObject;

public class MVideoSavedItem {

	private String uniqueId;
	private String title;
	private String artistName;
	private String videoThumb;
	private String viewCount;
	private String length;
	private int lastWatchPosition;

	// CONSTRUCTOR

	public MVideoSavedItem(String pUniqueId, String pTitle, String pArtistName, String pVideoThumb, String pViewCount, String pLength, int pLastWatchPosition) {
		uniqueId = pUniqueId;
		title = pTitle;
		artistName = pArtistName;
		videoThumb = pVideoThumb;
		viewCount = pViewCount;
		length = pLength;
		lastWatchPosition = pLastWatchPosition;
	}
	
	// GETTER & SETTER
	
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getArtistName() {
		return artistName;
	}
	
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	
	public String getVideoThumb() {
		return videoThumb;
	}

	public void setVideoThumb(String videoThumb) {
		this.videoThumb = videoThumb;
	}

	public String getViewCount() {
		return viewCount;
	}
	
	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}
	
	public String getLength() {
		return length;
	}
	
	public void setLength(String length) {
		this.length = length;
	}
	
	public int getLastWatchPosition() {
		return lastWatchPosition;
	}
	
	public void setLastWatchPosition(int lastWatchPosition) {
		this.lastWatchPosition = lastWatchPosition;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		
		try {
			json.put("uniqueId", uniqueId);
			json.put("title", title);
			json.put("artistName", artistName);
			json.put("videoThumb", videoThumb);
			json.put("viewCount", viewCount);
			json.put("length", length);
			json.put("lastWatchPosition", lastWatchPosition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}
}
