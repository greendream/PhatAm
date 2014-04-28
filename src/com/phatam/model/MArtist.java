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

public class MArtist {
	private String artistName; 	// the artist name
	private String videoCount; 	// the video count of artist
	private String aboutArtist;	// the avatar link
	private String avatar;		// the avatar link

	public MArtist(String name, String cnt, String about, String avatar) {
		this.artistName = name;
		this.videoCount = cnt;
		this.aboutArtist = about;
		this.avatar = "http://phatam.com/rest/public/images/artist/" + avatar;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getVideoCount() {
		return videoCount;
	}

	public void setVideoCount(String videoCount) {
		this.videoCount = videoCount;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = "http://phatam.com/rest/public/images/artist/" + avatar;
	}

	public String getAboutArtist() {
		return aboutArtist;
	}

	public void setAboutArtist(String aboutArtist) {
		this.aboutArtist = aboutArtist;
	}
	
	
}
