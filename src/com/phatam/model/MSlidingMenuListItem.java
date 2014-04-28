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

import com.phatam.interfaces.OnSlidingMenuItemClickedListener;

public class MSlidingMenuListItem {
	public static final int ITEM_TYPE_CATEGORY_NAME = 0;
	public static final int ITEM_TYPE_SCREEN_IN_CATEGORY = 1;
	public static final int ITEM_TYPE_TOP_MENU_GROUP = 2;
	
	protected int mType;
	protected int mIconLeft;
	protected String mText;
	protected String mCounter;
	protected OnSlidingMenuItemClickedListener mOnItemClickedHandler;
	
	//
	protected OnSlidingMenuItemClickedListener actionHome;
	protected OnSlidingMenuItemClickedListener actionFavorite;
	protected OnSlidingMenuItemClickedListener actionHistory;
	
	public int getType() {
		return mType;
	}
	
	public void setType(int mType) {
		this.mType = mType;
	}
	
	public int getIconLeft() {
		return mIconLeft;
	}
	
	public void setIconLeft(int mIconLeft) {
		this.mIconLeft = mIconLeft;
	}
	
	public String getText() {
		return mText;
	}
	
	public void setText(String mText) {
		this.mText = mText;
	}
	
	public String getCounter() {
		return mCounter;
	}
	
	public void setCounter(String mCounter) {
		this.mCounter = mCounter;
	}
	
	public OnSlidingMenuItemClickedListener getOnItemClickHandler() {
		return mOnItemClickedHandler;
	}
	
	public void setOnItemClickHandler(OnSlidingMenuItemClickedListener mOnItemClickHandler) {
		this.mOnItemClickedHandler = mOnItemClickHandler;
	}
	
	public OnSlidingMenuItemClickedListener getActionHome() {
		return actionHome;
	}

	public void setActionHome(OnSlidingMenuItemClickedListener actionHome) {
		this.actionHome = actionHome;
	}

	public OnSlidingMenuItemClickedListener getActionFavorite() {
		return actionFavorite;
	}

	public void setActionFavorite(OnSlidingMenuItemClickedListener actionFavorite) {
		this.actionFavorite = actionFavorite;
	}

	public OnSlidingMenuItemClickedListener getActionHistory() {
		return actionHistory;
	}

	public void setActionHistory(OnSlidingMenuItemClickedListener actionHistory) {
		this.actionHistory = actionHistory;
	}

	public MSlidingMenuListItem(int mType, OnSlidingMenuItemClickedListener pActionHome, OnSlidingMenuItemClickedListener pActionFavorite, OnSlidingMenuItemClickedListener pActionHistory) {
		this.mType = mType;
		actionHome = pActionHome;
		actionFavorite = pActionFavorite;
		actionHistory = pActionHistory;
	}

	public MSlidingMenuListItem(int mType, int mIconLeft, String mText, String mCounter, OnSlidingMenuItemClickedListener mOnItemClickHandler) {
		this.mType = mType;
		this.mIconLeft = mIconLeft;
		this.mText = mText;
		this.mCounter = mCounter;
		this.mOnItemClickedHandler = mOnItemClickHandler;
	}
	
}