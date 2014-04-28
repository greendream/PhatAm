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
import com.phatam.adapters.EpisodeAdapter;
import com.phatam.model.MVideoItem;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("ValidFragment")
public class FEpisodeListView extends SherlockFragment {
	private MVideoItem mVideoItem;
	private ListView mEpisodeListView;
	private View mContentView;
	private EpisodeAdapter mEpisodeAdapter;

	// Event handler
	private OnItemClickListener OnVideoListItemClick;
	
	/**
	 * Disable StricMode
	 */
	static {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}
	
	public FEpisodeListView() {
	}

	public FEpisodeListView(MVideoItem videoItem) {
		mVideoItem = videoItem;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_episodes, container, false);
		mEpisodeListView = (ListView) mContentView.findViewById(R.id.episode_list_view);
		mEpisodeAdapter = new EpisodeAdapter(getSherlockActivity(), mVideoItem);
		mEpisodeListView.setAdapter(mEpisodeAdapter);
		mEpisodeListView.setOnItemClickListener(OnVideoListItemClick);

		return mContentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Get CONCURRENT_PAGE_AVAILABLE page and put to VideoAdapter

	}

	public void setOnVideoItemClicked(OnItemClickListener onItemClick) {
		OnVideoListItemClick = onItemClick;
	}

}
