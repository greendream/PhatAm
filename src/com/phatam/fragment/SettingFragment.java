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
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.phatam.activities.PagerListVideoActivity;
import com.phatam.adapters.AuthorAdapter;
import com.phatam.customviews.LoadMoreListView;
import com.phatam.customviews.LoadMoreListView.OnLoadMoreListener;
import com.phatam.entities.ArtistItem;
import com.phatam.interfaces.OnRefreshListData;
import com.phatam.websevice.ApiUrl;


@SuppressLint("NewApi")
public class SettingFragment extends SherlockFragment implements OnRefreshListData {
		
	// Pager component
	private static final int PAGE_SIZE = ApiUrl.ARTIST_PAGE_SIZE;
	private int mEndPageIndex = 0;
	
	// ListView component
	public ArrayList<ArtistItem> mArrayListArtistItems;
	private AuthorAdapter mAuthorListViewAdapter;
	private LoadMoreListView mLoadMoreListView;
	private ProgressBar mProgressBar;
	private TextView tvHasNoResult;
	private String mUrl;
	
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
	
	public SettingFragment() {
	}
	
	/**
	 * You need provide URL has full link has "/" at the end except OFFSET field
	 * 	
	 * @param url
	 * @return 
	 */
	public SettingFragment setUrl(String url) {
		mUrl = url;
		return this;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		isLoadNew = true;
		if (mArrayListArtistItems == null) {
			mArrayListArtistItems = new ArrayList<ArtistItem>();
		} else {
			isLoadNew = false;
		}
		
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_author_load_more_list, container, false);
		
		mProgressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
		mProgressBar.setVisibility(View.VISIBLE);

		tvHasNoResult = (TextView) rootView.findViewById(R.id.tvHasNoResult);
		tvHasNoResult.setVisibility(View.GONE);
		
		mLoadMoreListView = (LoadMoreListView) rootView.findViewById(R.id.list_view_author);
		mLoadMoreListView.setVisibility(View.GONE);
		mLoadMoreListView.setOnItemClickListener(onAuthorListItemClicked);
		mArrayListArtistItems = new ArrayList<ArtistItem>();
		mAuthorListViewAdapter = new AuthorAdapter(getSherlockActivity(), mArrayListArtistItems);
		mLoadMoreListView.setAdapter(mAuthorListViewAdapter);
		
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
			mAuthorListViewAdapter.notifyDataSetChanged();
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
	private ArrayList<ArtistItem> getAuthorInPage(int pageIndex) {
		ArrayList<ArtistItem> mArtistItems = new ArrayList<ArtistItem>();

		String url = mUrl + (pageIndex * PAGE_SIZE);
		
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
			
			// Convert response data to JSON
			JSONObject result = new JSONObject(response);
			JSONArray jArray;
			
			try {
				jArray = result.getJSONArray("videos");
			}catch (JSONException e) {
				jArray = result.getJSONArray("result");
			}
			
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject line_object = jArray.getJSONObject(i);
				String strArtist = line_object.getString("artist");
				String cnt = line_object.getString("cnt");
				ArtistItem item = new ArtistItem(strArtist, cnt);
				mArtistItems.add(item);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mArtistItems;
	}

	private class LoadMoreDataTask extends AsyncTask<Void, Void, Void> {
		ArrayList<ArtistItem> arrLoadmoreAuthorPage;
		
		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

			// Step 1. Get PAGE_SIZE video of the page after mEndPageIndex to
			// AuthorAdapter
			arrLoadmoreAuthorPage = getAuthorInPage(mEndPageIndex++);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// hide load more progress at the end of list
			if (arrLoadmoreAuthorPage.size() == 0) {
				mLoadMoreListView.hideFooter();
				mLoadMoreListView.setVisibility(View.GONE);
				tvHasNoResult.setVisibility(View.VISIBLE);
			} else {
				mLoadMoreListView.setVisibility(View.VISIBLE);
				tvHasNoResult.setVisibility(View.GONE);
							
				// when end of result
				if (arrLoadmoreAuthorPage.size() < PAGE_SIZE) {
					mLoadMoreListView.hideFooter();
				}
							
				if (arrLoadmoreAuthorPage.size() > 0) {
					mArrayListArtistItems.addAll(arrLoadmoreAuthorPage);
					
					// We need notify the adapter that the data have been changed
					mAuthorListViewAdapter.notifyDataSetChanged();
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
	
	private OnItemClickListener onAuthorListItemClicked = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

			Intent i = new Intent(getActivity(), PagerListVideoActivity.class);
			i.putExtra(PagerListVideoActivity.STR_URL, ApiUrl.getAllVideoOfArtistUrl(mArrayListArtistItems.get(position).getAristName(), "", -1));
			i.putExtra(PagerListVideoActivity.STR_ACTION_BAR_TITLE, mArrayListArtistItems.get(position).getAristName());
			i.putExtra(PagerListVideoActivity.STR_DEFAULT_PAGE_INDEX, 2);
			getActivity().startActivity(i);
		}
	};
	


	@Override
	public void onRefreshListDataByUrl(String url) {
		// TODO Auto-generated method stub
		mUrl = url;
		mEndPageIndex = 0;
		mProgressBar.setVisibility(View.VISIBLE);
		try {
			mArrayListArtistItems.removeAll(mArrayListArtistItems);
			mAuthorListViewAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			if (BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		}
		
		// Get CONCURRENT_PAGE_AVAILABLE page and put to VideoAdapter
		new LoadMoreDataTask().execute();
	}
}
