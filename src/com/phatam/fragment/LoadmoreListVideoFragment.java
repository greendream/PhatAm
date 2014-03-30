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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.phatam.BuildConfig;
import com.phatam.R;
import com.phatam.activities.FullVideoInfoActivity;
import com.phatam.adapters.ListVideoAdapter;
import com.phatam.customviews.LoadMoreListView;
import com.phatam.customviews.LoadMoreListView.OnLoadMoreListener;
import com.phatam.entities.VideoItem;
import com.phatam.interfaces.OnRefreshListData;
import com.phatam.websevice.ApiUrl;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("ValidFragment")
public class LoadmoreListVideoFragment extends SherlockFragment implements OnRefreshListData{
	// About load content
	private static final int PAGE_SIZE = ApiUrl.VIDEO_PAGE_SIZE;
	private int mEndPageIndex = 0;

	private ArrayList<VideoItem> mArrayListVideoItems;
	private LoadMoreListView mLoadMoreListView;
	private ListVideoAdapter mVideoListViewAdapter;
	private ProgressBar mProgressBar;
	private TextView tvHasNoResult;
	public String mUrl;

	private boolean isLoadNew;
	
	/**
	 * Disable StricMode
	 */
	static {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}
	
	public LoadmoreListVideoFragment() {
	}
	
	/**
	 * You need provide URL has full link has "/" at the end except OFFSET field
	 * 	
	 * @param url
	 */
	public LoadmoreListVideoFragment setUrl(String url) {
		mUrl = url;
		return this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		isLoadNew = true;
		if (mArrayListVideoItems == null) {
			mArrayListVideoItems = new ArrayList<VideoItem>();
		} else {
			isLoadNew = false;
		}
		
		View rootView = inflater.inflate(R.layout.fragment_load_more_list_video, container, false);
		
		mProgressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
		mProgressBar.setVisibility(View.VISIBLE);
		
		tvHasNoResult = (TextView) rootView.findViewById(R.id.tvHasNoResult);
		tvHasNoResult.setVisibility(View.GONE);
		
		mLoadMoreListView = (LoadMoreListView) rootView.findViewById(R.id.load_more_list_view);
		mLoadMoreListView.setVisibility(View.GONE);	
		mArrayListVideoItems = new ArrayList<VideoItem>();
		mVideoListViewAdapter = new ListVideoAdapter(getSherlockActivity(), mArrayListVideoItems);
		mLoadMoreListView.setAdapter(mVideoListViewAdapter);
		mLoadMoreListView.setOnItemClickListener(onVideoListItemClicked);

		// set a listener to be invoked when the list reaches the end
		mLoadMoreListView.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				// In the AsyncTask we will be PAGE_SIZE video of the page after mEndPageIndex
				new LoadMoreDataTask().execute();
			}
		});

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (isLoadNew) {			
			mLoadMoreListView.onLoadMore();
		} else {
			mVideoListViewAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * Get video list from server and create new mListVideoItem for adapter to
	 * show the list view to UI
	 * 
	 * @param url
	 * @param orderbyType
	 * @param pageIndex
	 *            : page index in result list
	 */
	private ArrayList<VideoItem> getVideoInPage(int pageIndex) {
		ArrayList<VideoItem> mVideoItems = new ArrayList<VideoItem>();
		if (mUrl == null) {
			return mVideoItems;
		}
		
		String url = "";
		
		/**********************************************************
		 * Will be replace by
		 * 
		 * String url = mUrl + (pageIndex * PAGE_SIZE);
		 * when API completed
		 */
		if (mUrl.indexOf("randomvideos") > 0) {
			url = mUrl;	
		} else {

			url = mUrl + (pageIndex * PAGE_SIZE);
		}
		/*********************************************************/
		
		Log.i("START RUN GET JSON TASK URL", url);
		String response = "";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

		try {
			HttpResponse execute = client.execute(httpGet);
			InputStream content = execute.getEntity().getContent();

			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					content));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
			}

			JSONObject result = new JSONObject(response);
			JSONArray jArray;
			
			jArray = result.getJSONArray("videos");
			
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject line_object = jArray.getJSONObject(i);
				String uniq_id = line_object.getString("uniq_id");
				String artist = line_object.getString("artist");
				String video_title = line_object.getString("video_title");
				String description = line_object.getString("description");
				String yt_thumb = line_object.getString("yt_thumb");
				int site_views = line_object.getInt("site_views");
				String yt_id = line_object.getString("yt_id");
				String category = line_object.getString("category");
				VideoItem item = new VideoItem(uniq_id, artist, video_title,
						description, yt_id, yt_thumb, site_views, category);

				mVideoItems.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mVideoItems;
	}

	private class LoadMoreDataTask extends AsyncTask<Void, Void, Void> {
		ArrayList<VideoItem> arrLoadmoreVideoPage;
		
		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

			// Step 1. Get PAGE_SIZE video of the page after mEndPageIndex to
			// mListVideoAdapter
			arrLoadmoreVideoPage = getVideoInPage(mEndPageIndex++);			
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// hide load more progress at the end of list
			if (arrLoadmoreVideoPage.size() == 0) {
				mLoadMoreListView.hideFooter();
				mLoadMoreListView.setVisibility(View.GONE);
				tvHasNoResult.setVisibility(View.VISIBLE);
			} else {
				mLoadMoreListView.setVisibility(View.VISIBLE);
				tvHasNoResult.setVisibility(View.GONE);
				
				// when end of result
				if (arrLoadmoreVideoPage.size() < PAGE_SIZE) {
					mLoadMoreListView.hideFooter();
				}
				
				if (arrLoadmoreVideoPage.size() > 0) {
					mArrayListVideoItems.addAll(arrLoadmoreVideoPage);

					// We need notify the adapter that the data have been changed
					mVideoListViewAdapter.notifyDataSetChanged();

				}
			}


			mLoadMoreListView.onLoadMoreComplete();
			mProgressBar.setVisibility(View.GONE);
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			mLoadMoreListView.onLoadMoreComplete();
			mProgressBar.setVisibility(View.GONE);
		}
	}

	private OnItemClickListener onVideoListItemClicked = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			Intent i = new Intent(getSherlockActivity(), FullVideoInfoActivity.class);
			i.putExtra(FullVideoInfoActivity.INFO_VIDEO_UNIQUE_ID, mArrayListVideoItems.get(position).getUniqueId());
			startActivity(i);
		}
	};

	@Override
	public void onRefreshListDataByUrl(String url) {
		mUrl= url;
		mEndPageIndex = 0;
		mProgressBar.setVisibility(View.VISIBLE);
		try {
			mArrayListVideoItems.removeAll(mArrayListVideoItems);
			mVideoListViewAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			if (BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		}
		
		// Get CONCURRENT_PAGE_AVAILABLE page and put to VideoAdapter
		new LoadMoreDataTask().execute();
	}


}
