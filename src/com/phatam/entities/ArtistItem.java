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

public class ArtistItem {
	private String mName; 	// the artist name
	private String mCnt; 	// the video count of artist

	public ArtistItem(String name, String cnt) {
		this.mName = name;
		this.mCnt = cnt;
	}

	public String getAristName() {
		return this.mName;
	}

	public String getVideoCount() {
		return this.mCnt;
	}
}