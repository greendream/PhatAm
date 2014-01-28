package com.phatam.entity;

public class Episode {
	int episode_id;
	String episode;
	String uniq_id;
	String yt_id;
	String yt_thumb;

	public Episode(String uniq_id, int episode_id, String yt_id, String youtube_image, String episode) {
		this.uniq_id = uniq_id;
		this.episode_id = episode_id;
		this.yt_id = yt_id;
		this.yt_thumb = youtube_image;
		this.episode = episode;
	}

	public String getEpisode() {
		return this.episode;
	}

	public String getVideoId() {
		return this.uniq_id;
	}

	public int getEpisodeId() {
		return this.episode_id;
	}

	public String getYoutubeId() {
		return this.yt_id;
	}
	
	public String getYoutubeThumb() {
		return this.yt_thumb;
	}
}
