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

public class MCategoryReviewItem {
	public final static int TYPE_FOUR_VIDEOS = 0;
	public final static int TYPE_SIX_VIDEOS = 1;
	
	private int categoryType;
	private String urlGetVideoList;
	private String categoryId;
	private String categoryName;

	private ArrayList<MVideoItem> mLoadedVideoList = new ArrayList<MVideoItem>();

	// CONSTRUCTOR
	public MCategoryReviewItem() {
		
	}
	
	public ArrayList<MVideoItem> getmLoadedVideoList() {
		return mLoadedVideoList;
	}

	public void setLoadedVideoList(ArrayList<MVideoItem> mLoadedVideoList) {
		this.mLoadedVideoList = mLoadedVideoList;
	}

	public MCategoryReviewItem(String pUrlGetVideoList, String pCategoryId, String pCategoryName, int pCategoryType) {
		urlGetVideoList = pUrlGetVideoList;
		categoryId = pCategoryId;
		categoryName = pCategoryName;
		categoryType = pCategoryType;
	}

	// GETTER & SETTER
	public String getUrlGetVideoList() {
		return urlGetVideoList;
	}

	public void setUrlGetVideoList(String urlGetVideoList) {
		this.urlGetVideoList = urlGetVideoList;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName.toUpperCase();
	}

	public int getCategoryType() {
		return categoryType;
	}

	/**
	 * Set the category type
	 * 
	 * @param categoryType CategoryReviewModel.TYPE_SIX_VIDEOS | CategoryReviewModel.TYPE_FOUR_VIDEOS
	 */
	public void setCategoryType(int categoryType) {
		this.categoryType = categoryType;
	}
	
	
}
