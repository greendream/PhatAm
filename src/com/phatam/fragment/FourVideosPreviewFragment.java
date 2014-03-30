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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.phatam.BuildConfig;
import com.phatam.R;
import com.phatam.activities.FullVideoInfoActivity;
import com.phatam.activities.PagerListVideoActivity;
import com.phatam.config.GlobalData;
import com.phatam.entities.VideoItem;
import com.phatam.interfaces.OnTimerTickListeners;
import com.phatam.util.UtilApplication;
import com.phatam.websevice.ApiUrl;

public class FourVideosPreviewFragment extends SherlockFragment implements
		OnTimerTickListeners, OnClickListener {
	public static String CLASS_PATH = VideoBannerAutoSlidingFragment.class.toString();
	
	private String mUrl;
	private int mCategoryIndex;
	private String mCategoryName;
	private int mTurn;

	private ArrayList<VideoItem> mVideoList = new ArrayList<VideoItem>();
	private ArrayList<Integer> mNext4Indexs = new ArrayList<Integer>();
	private TextView tvCategoryName;

	private ImageView ivPreview1;
	private TextView tvVideoTitle1;
	private TextView tvVideoArtist1;

	private ImageView ivPreview2;
	private TextView tvVideoTitle2;
	private TextView tvVideoArtist2;

	private ImageView ivPreview3;
	private TextView tvVideoTitle3;
	private TextView tvVideoArtist3;

	private ImageView ivPreview4;
	private TextView tvVideoTitle4;
	private TextView tvVideoArtist4;

	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener1 = new AnimateFirstDisplay1Listener();
	private ImageLoadingListener animateFirstListener2 = new AnimateFirstDisplay2Listener();
	private ImageLoadingListener animateFirstListener3 = new AnimateFirstDisplay3Listener();
	private ImageLoadingListener animateFirstListener4 = new AnimateFirstDisplay4Listener();

	public void setUrlGetVideoList(String pUrl) {
		mUrl = pUrl;
	}

	private void setVideoList(ArrayList<VideoItem> pVideoList) {
		mVideoList = pVideoList;
		mTurn = 0;

		// Get next 4 videos in the video list to preview
		mNext4Indexs = getNext4Indexs(mVideoList);

		// Show video preview info
		try {
			showVideoPreviewInfo(mVideoList, mNext4Indexs);
		} catch (Exception e) {
			
		}

	}

	/**
	 * Get next 4 videos in the video list to preview
	 * 
	 * @param pVideoList
	 * @return
	 */
	private ArrayList<Integer> getNext4Indexs(ArrayList<VideoItem> pVideoList) {
		ArrayList<Integer> next4Indexs = new ArrayList<Integer>();

		if (pVideoList.size() < 4) {
			try {
				next4Indexs.add(0);
				return next4Indexs;
			} catch (Exception e) {

			}
		}

		mTurn = mTurn % (pVideoList.size() - 4 + 1);

		next4Indexs.add(mTurn);
		next4Indexs.add(mTurn + 1);
		next4Indexs.add(mTurn + 2);
		next4Indexs.add(mTurn + 3);
		mTurn++;

		return next4Indexs;
	}

	/**
	 * Show video preview info
	 * 
	 * @param pVideoList
	 * @param p4RandomNewVideoList
	 */
	private void showVideoPreviewInfo(ArrayList<VideoItem> pVideoList, ArrayList<Integer> p4RandomNewVideoList) throws Exception {
		GlobalData.imageLoader.displayImage(pVideoList.get(p4RandomNewVideoList.get(0)).getYoutubeThumb(), ivPreview1, options, animateFirstListener1);
		tvVideoTitle1.setText(pVideoList.get(p4RandomNewVideoList.get(0)).getVideoTitle());
		tvVideoArtist1.setText(pVideoList.get(p4RandomNewVideoList.get(0)).getVideoArtist());

		GlobalData.imageLoader.displayImage(pVideoList.get(p4RandomNewVideoList.get(1)).getYoutubeThumb(),ivPreview2, options, animateFirstListener2);
		tvVideoTitle2.setText(pVideoList.get(p4RandomNewVideoList.get(1)).getVideoTitle());
		tvVideoArtist2.setText(pVideoList.get(p4RandomNewVideoList.get(1)).getVideoArtist());

		GlobalData.imageLoader.displayImage(pVideoList.get(p4RandomNewVideoList.get(2)).getYoutubeThumb(),ivPreview3, options, animateFirstListener3);
		tvVideoTitle3.setText(pVideoList.get(p4RandomNewVideoList.get(2)).getVideoTitle());
		tvVideoArtist3.setText(pVideoList.get(p4RandomNewVideoList.get(2)).getVideoArtist());

		GlobalData.imageLoader.displayImage(pVideoList.get(p4RandomNewVideoList.get(3)).getYoutubeThumb(), ivPreview4, options, animateFirstListener4);
		tvVideoTitle4.setText(pVideoList.get(p4RandomNewVideoList.get(3)).getVideoTitle());
		tvVideoArtist4.setText(pVideoList.get(p4RandomNewVideoList.get(3)).getVideoArtist());
	}

	/**
	 * Set category name
	 * 
	 * @param name
	 */
	public void setCategoryName(String name) {
		mCategoryName = name.toUpperCase();
	}

	/**
	 * Set category index
	 * 
	 * @param index
	 */
	public void setCategoryIndex(int index) {
		mCategoryIndex = index;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_four_videos_preview,
				container, false);

		// Category name
		tvCategoryName = (TextView) rootView.findViewById(R.id.tvCategoryName);
		tvCategoryName.setText(mCategoryName);
		tvCategoryName.setOnClickListener(this);

		ivPreview1 = (ImageView) rootView.findViewById(R.id.ivPreview1);
		ivPreview1.setOnClickListener(this);
		tvVideoTitle1 = (TextView) rootView.findViewById(R.id.tvVideoTitle1);
		tvVideoArtist1 = (TextView) rootView.findViewById(R.id.tvVideoArtist1);

		ivPreview2 = (ImageView) rootView.findViewById(R.id.ivPreview2);
		ivPreview2.setOnClickListener(this);
		tvVideoTitle2 = (TextView) rootView.findViewById(R.id.tvVideoTitle2);
		tvVideoArtist2 = (TextView) rootView.findViewById(R.id.tvVideoArtist2);

		ivPreview3 = (ImageView) rootView.findViewById(R.id.ivPreview3);
		ivPreview3.setOnClickListener(this);
		tvVideoTitle3 = (TextView) rootView.findViewById(R.id.tvVideoTitle3);
		tvVideoArtist3 = (TextView) rootView.findViewById(R.id.tvVideoArtist3);

		ivPreview4 = (ImageView) rootView.findViewById(R.id.ivPreview4);
		ivPreview4.setOnClickListener(this);
		tvVideoTitle4 = (TextView) rootView.findViewById(R.id.tvVideoTitle4);
		tvVideoArtist4 = (TextView) rootView.findViewById(R.id.tvVideoArtist4);

		return rootView;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		(new GetNewVideoJSONTask()).execute();

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).build();

	}

	@Override
	public void onStop() {
		super.onStop();
	}

	private ArrayList<VideoItem> getNewVideo() {
		ArrayList<VideoItem> mVideoItems = new ArrayList<VideoItem>();
		if (mUrl == null) {
			return mVideoItems;
		}

		// Get video JSON info
		String response = "";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(mUrl);
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
			UtilApplication.saveJsonToInternalMemory(result.toString(), CLASS_PATH + "_" + mCategoryIndex + ".json", getActivity());
			

		} catch (Exception e) {
			// Announce Connection error
			
			// Load saved data
			result = UtilApplication.loadJsonFromInternalMemory(CLASS_PATH + "_" + mCategoryIndex + ".json", getActivity());
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

			if (isCancelled()) {
				return null;
			}

			mVideoList = getNewVideo();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			setVideoList(mVideoList);
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {

		}
	}

	@Override
	public void onTimerTick() {
		try {
			// Get random 4 videos in the video list to preview
			mNext4Indexs = getNext4Indexs(mVideoList);

			// Show video preview info
			showVideoPreviewInfo(mVideoList, mNext4Indexs);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		try {
			v.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.animator.fade_zoom_in));
			Intent itentFullVideoInfoActivity = new Intent(getSherlockActivity(), FullVideoInfoActivity.class);

			switch (v.getId()) {
			case R.id.tvCategoryName:
				Intent itentPagerListVideoActivity = new Intent(getActivity(), PagerListVideoActivity.class);
				itentPagerListVideoActivity.putExtra(PagerListVideoActivity.STR_URL, ApiUrl.getVideoInCategoryUrl(getActivity().getResources().getStringArray(R.array.arr_cartegory_id)[mCategoryIndex], "", 1));
				itentPagerListVideoActivity.putExtra(PagerListVideoActivity.STR_ACTION_BAR_TITLE, getActivity().getResources().getStringArray(R.array.arr_cartegory_name)[mCategoryIndex]);
				itentPagerListVideoActivity.putExtra(PagerListVideoActivity.STR_DEFAULT_PAGE_INDEX, 2);
				startActivity(itentPagerListVideoActivity);
				break;
			case R.id.ivPreview1:
				itentFullVideoInfoActivity.putExtra(FullVideoInfoActivity.INFO_VIDEO_UNIQUE_ID, mVideoList.get(mNext4Indexs.get(0)).getUniqueId());
				startActivity(itentFullVideoInfoActivity);
				break;
			case R.id.ivPreview2:
				itentFullVideoInfoActivity.putExtra(FullVideoInfoActivity.INFO_VIDEO_UNIQUE_ID, mVideoList.get(mNext4Indexs.get(1)).getUniqueId());
				startActivity(itentFullVideoInfoActivity);
				break;
			case R.id.ivPreview3:
				itentFullVideoInfoActivity.putExtra(FullVideoInfoActivity.INFO_VIDEO_UNIQUE_ID, mVideoList.get(mNext4Indexs.get(2)).getUniqueId());
				startActivity(itentFullVideoInfoActivity);
				break;
			case R.id.ivPreview4:
				itentFullVideoInfoActivity.putExtra(FullVideoInfoActivity.INFO_VIDEO_UNIQUE_ID, mVideoList.get(mNext4Indexs.get(3)).getUniqueId());
				startActivity(itentFullVideoInfoActivity);
				break;
			}

		} catch (Exception e) {
			if (BuildConfig.DEBUG) e.printStackTrace();
		}
	}

	private static class AnimateFirstDisplay1Listener extends
			SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());
		static final Animation anim = AnimationUtils.loadAnimation(
				GlobalData.context, R.animator.top_to_bottom);

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					displayedImages.add(imageUri);
				}
				imageView.startAnimation(anim);
			}
		}
	}

	private static class AnimateFirstDisplay2Listener extends
			SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());
		static final Animation anim = AnimationUtils.loadAnimation(
				GlobalData.context, R.animator.right_to_left);

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					displayedImages.add(imageUri);
				}
				imageView.startAnimation(anim);
			}
		}
	}

	private static class AnimateFirstDisplay3Listener extends
			SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());
		static final Animation anim = AnimationUtils.loadAnimation(
				GlobalData.context, R.animator.bottom_to_top);

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					displayedImages.add(imageUri);
				}
				imageView.startAnimation(anim);
			}
		}
	}

	private static class AnimateFirstDisplay4Listener extends
			SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());
		static final Animation anim = AnimationUtils.loadAnimation(
				GlobalData.context, R.animator.left_to_right);

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					displayedImages.add(imageUri);
				}

				imageView.startAnimation(anim);
			}
		}
	}
}
