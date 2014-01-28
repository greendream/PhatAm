package com.phatam.entity;

import java.util.ArrayList;

public class VideoItem extends VideoInfo {
	private ArrayList<VideoItem> related;
	private ArrayList<VideoItem> same_artist;
	private ArrayList<VideoItem> best_in_category;
	private ArrayList<Episode> episodes;
	private ArrayList<Tag> tags;

	public void sortEpisodeIncByEpisodeId() {
		for (int i = 0; i < episodes.size() - 1; i++)
			for (int j = i + 1; j < episodes.size(); j++){
				if (episodes.get(i).getEpisodeId() > episodes.get(j).getEpisodeId()) {
					Episode tmp = episodes.get(i);
					episodes.set(i, episodes.get(j));
					episodes.set(j, tmp);					
				}
			}
	}
	
	public ArrayList<Episode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(ArrayList<Episode> episodes) {
		this.episodes = episodes;
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}

	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}

	public ArrayList<VideoItem> getRelated() {
		return related;
	}

	// Use for relate videos
	public VideoItem(String uniq_id, String artist, String video_title,
			String description, String yt_id, String yt_thumb, int site_views, String category) {
		this.uniq_id = uniq_id;
		this.artist = artist;
		this.video_title = video_title;
		this.description = description;
		this.yt_id = yt_id;
		this.yt_thumb = yt_thumb;
		this.site_views = site_views;
		this.category = category;
	}

	// Use for get video from category
	public VideoItem(int id, String uniq_id, String artist, String video_title,
			String description, String yt_id, String yt_thumb, int site_views,
			String mp3) {
		this.id = id;
		this.uniq_id = uniq_id;
		this.artist = artist;
		this.video_title = video_title;
		this.description = description;
		this.yt_id = yt_id;
		this.yt_thumb = yt_thumb;
		this.site_views = site_views;
		this.mp3 = mp3;
	}

	public VideoItem() {
		this.id = -1;
		this.uniq_id = "";
		this.artist = "";
		this.video_title = "";
		this.description = "";
		this.yt_id = "";
		this.yt_thumb = "";
		this.site_views = 0;
		this.mp3 = "";
	}

	// Use for full video description
	public void setRelated(ArrayList<VideoItem> related) {
		this.related = related;
	}

	public void setSameArtist(ArrayList<VideoItem> same_artist) {
		this.same_artist = same_artist;
	}

	public void setBestInCategory(ArrayList<VideoItem> best_in_category) {
		this.best_in_category = best_in_category;
	}

	public void setEpisode(ArrayList<Episode> episodes) {
		this.episodes = episodes;
	}

	public void setTag(ArrayList<Tag> tags) {
		this.tags = tags;
	}
}
