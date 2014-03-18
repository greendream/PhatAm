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


package com.phatam.entities;

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
	protected String submitted;

	public int getId() {
		return this.id;
	}
	
	public String getCategory() {
		return this.category;
	}

	public String getUniqueId() {
		return this.uniq_id;
	}

	public String getVideoArtist() {
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

	public String getYoutubeThumb() {
		return this.yt_thumb;
	}

	public int getYoutubeView() {
		return this.site_views;
	}

	public String getMp3() {
		return this.mp3;
	}
	
	public String setSubbmited() {
		return this.submitted;
	}
	
	public void setUniqueId(String id){
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
	public void setYoutubeThumb(String img){
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
	
	public void setSubbmited(String submitted) {
		this.submitted = submitted;
	}
	
	
}
