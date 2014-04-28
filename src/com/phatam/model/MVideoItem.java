/*
 * Copyright (C) 2014  Le Ngoc Anh <greendream.ait@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 */


package com.phatam.model;

import java.util.ArrayList;
import java.util.Collections;

public class MVideoItem extends MVideoInfo {
	private ArrayList<MVideoItem> related;
//	private ArrayList<MVideoItem> same_artist;
//	private ArrayList<MVideoItem> best_in_category;
	private ArrayList<MEpisode> episodes;
	private ArrayList<MTag> tags;
	private int episodeId = 1;

	
	public int getEpisodeId() {
		return episodeId;
	}

	public void setEpisodeId(int episodeId) {
		this.episodeId = episodeId;
	}

	public void sortEpisodeIncByEpisodeId() {
		Collections.sort(episodes);
	}
	
	public ArrayList<MEpisode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(ArrayList<MEpisode> episodes) {
		this.episodes = episodes;
	}

	public ArrayList<MTag> getTags() {
		return tags;
	}

	public void setTags(ArrayList<MTag> tags) {
		this.tags = tags;
	}

	public ArrayList<MVideoItem> getRelated() {
		return related;
	}

	// Use for relate videos
	public MVideoItem(String uniq_id, String artist, String video_title,
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
	public MVideoItem(int id, String uniq_id, String artist, String video_title,
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

	public MVideoItem() {
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
	public void setRelated(ArrayList<MVideoItem> related) {
		this.related = related;
	}

//	public void setSameArtist(ArrayList<MVideoItem> same_artist) {
//		this.same_artist = same_artist;
//	}
//
//	public void setBestInCategory(ArrayList<MVideoItem> best_in_category) {
//		this.best_in_category = best_in_category;
//	}

	public void setEpisode(ArrayList<MEpisode> episodes) {
		this.episodes = episodes;
	}

	public void setTag(ArrayList<MTag> tags) {
		this.tags = tags;
	}
}
