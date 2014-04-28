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
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.phatam.R;
import com.phatam.activities.FullVideoInfoActivity;
import com.phatam.adapters.ListFavoriteVideosAdapter;
import com.phatam.config.GlobalData;
import com.phatam.interfaces.OnDataSetChange;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("ValidFragment")
public class FFavoriteVideosListView extends SherlockFragment implements OnDataSetChange{
	private ListView mFavoriteVideosListView;
	private View mContentView;	
	private ListFavoriteVideosAdapter mListFavoriteVideosAdapter;
	
	/**
	 * Disable StricMode
	 */
	static {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
	}
	
	public FFavoriteVideosListView() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_relative_video, container, false);
		mFavoriteVideosListView = (ListView) mContentView.findViewById(R.id.relative_video_list_view);

		mListFavoriteVideosAdapter = new ListFavoriteVideosAdapter(getSherlockActivity(), GlobalData.arrFavoriteVideos);
		mFavoriteVideosListView.setAdapter(mListFavoriteVideosAdapter);
		mFavoriteVideosListView.setOnItemClickListener(mOnItemClickListener);
		
		GlobalData.onFavoriteChange = this;
		return mContentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Get CONCURRENT_PAGE_AVAILABLE page and put to VideoAdapter
		 

	}
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			Intent i = new Intent(getSherlockActivity(), FullVideoInfoActivity.class);
			i.putExtra(FullVideoInfoActivity.INFO_VIDEO_UNIQUE_ID, GlobalData.arrFavoriteVideos.get(position).getUniqueId());
			startActivity(i);
		}
	};

	@Override
	public void onDataSetChange() {
		// TODO Auto-generated method stub
		mListFavoriteVideosAdapter.notifyDataSetChanged();
	}
}
