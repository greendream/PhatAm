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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.phatam.BuildConfig;
import com.phatam.R;
import com.phatam.activities.FullVideoInfoActivity;
import com.phatam.adapters.ImagesGalleryFragmentPagerAdapter;
import com.phatam.entities.VideoItem;
import com.phatam.interfaces.OnTimerTickListeners;
import com.phatam.util.TimeTicker;
import com.phatam.util.UtilApplication;
import com.viewpagerindicator.CirclePageIndicator;

public class VideoBannerAutoSlidingFragment extends SherlockFragment implements
		OnTimerTickListeners, OnClickListener, OnPageChangeListener {

	public static String CLASS_PATH = VideoBannerAutoSlidingFragment.class.toString();
	private final static int NUMBER_OF_IMAGE_IN_GALLERY = 5;

	private FragmentManager mFragmentManager;
	private ImagesGalleryFragmentPagerAdapter mFragmentPagerAdapter;
	private ViewPager vpImages;
	private CirclePageIndicator mIndicator;
	private String mNewVideoUrl;
	private ArrayList<String> mImageUrls = new ArrayList<String>();
	private ArrayList<VideoItem> mVideoItems = new ArrayList<VideoItem>();
	private ImageButton ibPlay;
	private ProgressBar mProgressBar;
	private TextView tvVideoTitle;
	private TextView tvVideoArtist;

	private TimeTicker mTimeTicker;

	public void setImageUrls(ArrayList<String> pImageUrls) {
		mImageUrls = pImageUrls;
	}

	private void setVideoItems(ArrayList<VideoItem> pVideoItems) {
		mVideoItems = pVideoItems;
		mImageUrls = new ArrayList<String>();

		try {
			for (int i = 0; i < NUMBER_OF_IMAGE_IN_GALLERY; i++) {
				mImageUrls.add(mVideoItems.get(i).getYoutubeThumb());
			}

			mFragmentPagerAdapter.setImageUrls(mImageUrls);
			mFragmentPagerAdapter.notifyDataSetChanged();

			// Put video info
			tvVideoTitle.setText(mVideoItems.get(vpImages.getCurrentItem()).getVideoTitle());
			tvVideoArtist.setText(mVideoItems.get(vpImages.getCurrentItem()).getVideoArtist());

			// Create timer to control auto Sliding
			mTimeTicker = new TimeTicker(3000);
			mTimeTicker.addOnTimeTickerListener(this);
			mTimeTicker.start();
		} catch (Exception e) {
			if (BuildConfig.DEBUG) e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_gallery_auto_scroll,container, false);

		ibPlay = (ImageButton) rootView.findViewById(R.id.ibPlay);
		ibPlay.setVisibility(View.GONE);
		ibPlay.setOnClickListener(this);

		mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
		mProgressBar.setVisibility(View.VISIBLE);

		// Prepare fragment adapter
		mFragmentManager = getActivity().getSupportFragmentManager();
		mFragmentPagerAdapter = new ImagesGalleryFragmentPagerAdapter(mFragmentManager);
		mFragmentPagerAdapter.setImageUrls(mImageUrls);

		// Prepare ViewPager
		vpImages = (ViewPager) rootView.findViewById(R.id.vpImages);
		vpImages.setAdapter(mFragmentPagerAdapter);
		vpImages.setVisibility(View.GONE);
		vpImages.setOnPageChangeListener(this);

		// Prepare the circle Indicator
		mIndicator = (CirclePageIndicator) rootView.findViewById(R.id.circleIndicator);
		mIndicator.setViewPager(vpImages);
		mIndicator.setVisibility(View.GONE);

		// Video info
		tvVideoTitle = (TextView) rootView.findViewById(R.id.tvVideoTitle);
		tvVideoArtist = (TextView) rootView.findViewById(R.id.tvVideoArtist);

		return rootView;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		(new GetNewVideoJSONTask()).execute();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	public void setVideoSourceLink(String newVideoUrl) {
		// TODO Auto-generated method stub
		mNewVideoUrl = newVideoUrl + "0";
	}

	private ArrayList<VideoItem> getNewVideo() {
		ArrayList<VideoItem> mVideoItems = new ArrayList<VideoItem>();
		if (mNewVideoUrl == null) {
			return mVideoItems;
		}

		// Get video JSON info
		String response = "";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(mNewVideoUrl);
		JSONObject result = new JSONObject();
		
		try {
			HttpResponse execute = client.execute(httpGet);
			InputStream content = execute.getEntity().getContent();

			BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
			}

			// Create and save the result
			result = new JSONObject(response);
			UtilApplication.saveJsonToInternalMemory(result.toString(), CLASS_PATH + ".json", getActivity());
		} catch (Exception e) {
			// Announce Connection error
			
			// Load saved data
			result = UtilApplication.loadJsonFromInternalMemory(CLASS_PATH + ".json", getActivity());
		}
		
		// Get video info from JSONArray
		JSONArray jArray = new JSONArray();
		try {
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

	private class GetNewVideoJSONTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mProgressBar.setVisibility(View.VISIBLE);

			if (isCancelled()) {
				return null;
			}

			mVideoItems = getNewVideo();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mProgressBar.setVisibility(View.GONE);
			ibPlay.setVisibility(View.VISIBLE);
			mIndicator.setVisibility(View.VISIBLE);
			vpImages.setVisibility(View.VISIBLE);
			setVideoItems(mVideoItems);
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			mProgressBar.setVisibility(View.GONE);
			ibPlay.setVisibility(View.VISIBLE);
			mIndicator.setVisibility(View.VISIBLE);
			vpImages.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onTimerTick() {
		try {
			// Change to next Image in gallery smoothly
			int curIndex = vpImages.getCurrentItem();
			int size = mFragmentPagerAdapter.getCount();
			int newPageIndex = (curIndex + 1) % size;
			vpImages.setCurrentItem(newPageIndex, true);
		} catch (Exception e) {
			
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibPlay:
			try {
				v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.fade_zoom_in));
				Intent i = new Intent(getSherlockActivity(), FullVideoInfoActivity.class);
				i.putExtra(FullVideoInfoActivity.INFO_VIDEO_UNIQUE_ID, mVideoItems.get(vpImages.getCurrentItem()).getUniqueId());
				startActivity(i);
			} catch (Exception e) {
				
			}
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int position) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int pos) {
		try {
			tvVideoTitle.setText(mVideoItems.get(pos).getVideoTitle());
			tvVideoArtist.setText(mVideoItems.get(pos).getVideoArtist());
		} catch (Exception e) {
		}
	}
}
