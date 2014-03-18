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


package com.phatam.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.phatam.R;
import com.phatam.adapters.ListRelativeVideoAdapter;
import com.phatam.entities.VideoItem;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("ValidFragment")
public class RelativeVideoListViewFragment extends SherlockFragment {
	private ArrayList<VideoItem> mRelativeVideos;
	private ListView mRelativeVideoListView;
	private View mContentView;
	private ListRelativeVideoAdapter mListRelativeVideoAdapter;
	private OnItemClickListener mOnItemClickListener;
	/**
	 * Disable StricMode
	 */
	static {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
	}
	
	public RelativeVideoListViewFragment() {
	}

	public RelativeVideoListViewFragment(VideoItem videoItem) {
		mRelativeVideos = videoItem.getRelated();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_relative_video, container, false);
		mRelativeVideoListView = (ListView) mContentView.findViewById(R.id.relative_video_list_view);

		mListRelativeVideoAdapter = new ListRelativeVideoAdapter(getSherlockActivity(), mRelativeVideos);
		mRelativeVideoListView.setAdapter(mListRelativeVideoAdapter);
		mRelativeVideoListView.setOnItemClickListener(mOnItemClickListener);
		return mContentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Get CONCURRENT_PAGE_AVAILABLE page and put to VideoAdapter
		 

	}

	public void setOnVideoItemClicked(OnItemClickListener onItemClick) {
		mOnItemClickListener = onItemClick;
	}
	
}
