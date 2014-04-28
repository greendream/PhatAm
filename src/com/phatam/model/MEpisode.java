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

public class MEpisode implements Comparable<MEpisode> {
	int episode_id;
	String episode;
	String uniq_id;
	String yt_id;
	String yt_thumb;

	public MEpisode(String uniq_id, int episode_id, String yt_id, String youtube_image, String episode) {
		this.uniq_id = uniq_id;
		this.episode_id = episode_id;
		this.yt_id = yt_id;
		this.yt_thumb = youtube_image;
		this.episode = episode;
	}

	public String getEpisode() {
		return this.episode;
	}

	public String getUniqueId() {
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

	public void setYoutubeId(String yt_id) {
		this.yt_id = yt_id;
	}
	public void setYoutubeThumb(String yt_thumb) {
		 this.yt_thumb = yt_thumb;
	}
	public void setEpisodeId(int episode_id) {
		this.episode_id = episode_id; 
	}
	
	@Override
	public int compareTo(MEpisode another) {
		if (this.episode_id < another.getEpisodeId()) return -1;
		if (this.episode_id == another.getEpisodeId()) return 0;
		return 1;
	}
}
