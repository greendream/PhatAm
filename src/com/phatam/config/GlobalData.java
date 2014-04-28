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

package com.phatam.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.phatam.interfaces.OnDataSetChange;
import com.phatam.model.MCategoryReviewItem;
import com.phatam.model.MVideoSavedItem;
import com.phatam.util.UtilApplication;

public class GlobalData {
	
	public static Context context;
	public static ArrayList<MVideoSavedItem> arrHistoryVideos;
	public static ArrayList<MVideoSavedItem> arrFavoriteVideos;
	public static OnDataSetChange onHistoryChange;
	public static OnDataSetChange onFavoriteChange;
	
	/**
	 * Load video history from internal memory
	 * 
	 */
	public static void loadVideoHistoryList(Context ctx) {
		arrHistoryVideos = new ArrayList<MVideoSavedItem>();
		try {
			JSONObject json = UtilApplication.loadJsonFromInternalMemory("history.json", ctx);

			JSONArray history = json.getJSONArray("history");
			for (int i = 0; i < history.length(); i++) {
				String uniqueId = history.getJSONObject(i).getString("uniqueId");
				String title = history.getJSONObject(i).getString("title");
				String artistName = history.getJSONObject(i).getString("artistName");
				String videoThumb = history.getJSONObject(i).getString("videoThumb");
				String viewCount = history.getJSONObject(i).getString("viewCount");
				String length = history.getJSONObject(i).getString("length");
				int lastWatchPosition = history.getJSONObject(i).getInt("lastWatchPosition");
				MVideoSavedItem video = new MVideoSavedItem(uniqueId, title, artistName, videoThumb, viewCount, length, lastWatchPosition);
				
				arrHistoryVideos.add(video);
			}
			

			Log.i("ADD VIDEO", "Load json: " + json.toString() + "\n length = " + arrHistoryVideos.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Load video favorite from internal memory
	 * 
	 */
	public static void loadVideoFavoriteList(Context ctx) {
		arrFavoriteVideos = new ArrayList<MVideoSavedItem>();
		try {
			JSONObject json = UtilApplication.loadJsonFromInternalMemory("favorite.json", ctx);
			
			JSONArray favorite = json.getJSONArray("favorite");
			for (int i = 0; i < favorite.length(); i++) {
				String uniqueId = favorite.getJSONObject(i).getString("uniqueId");
				String title = favorite.getJSONObject(i).getString("title");
				String artistName = favorite.getJSONObject(i).getString("artistName");
				String videoThumb = favorite.getJSONObject(i).getString("videoThumb");
				String viewCount = favorite.getJSONObject(i).getString("viewCount");
				String length = favorite.getJSONObject(i).getString("length");
				int lastWatchPosition = favorite.getJSONObject(i).getInt("lastWatchPosition");
				MVideoSavedItem video = new MVideoSavedItem(uniqueId, title, artistName, videoThumb, viewCount, length, lastWatchPosition);
				
				arrFavoriteVideos.add(video);
			}
			
			Log.i("ADD VIDEO", "Load json: " + json.toString() + "\n length = " + arrFavoriteVideos.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save video history from internal memory
	 * 
	 */
	public static void saveVideoHistoryList(Context ctx) {

		JSONObject json = new JSONObject();
		JSONArray history = new JSONArray();
		try {
			for (MVideoSavedItem video : arrHistoryVideos) {
				history.put(video.toJson());
			}
			
			json.put("history", history);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Log.i("ADD VIDEO", "Save json: " + json);
		UtilApplication.saveJsonToInternalMemory(json.toString(), "history.json", ctx);
		
		try {
			onHistoryChange.onDataSetChange();
		} catch (Exception e) {
			
		}
	}

	/**
	 * Save video favorite from internal memory
	 * 
	 */
	public static void saveVideoFavoriteList(Context ctx) {
		JSONObject json = new JSONObject();
		JSONArray favorite = new JSONArray();
		try {
			for (MVideoSavedItem video : arrFavoriteVideos) {
				favorite.put(video.toJson());
			}
			
			json.put("favorite", favorite);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Log.i("ADD VIDEO", "Save json: " + json);
		UtilApplication.saveJsonToInternalMemory(json.toString(), "favorite.json", ctx);
		
		try {
			onFavoriteChange.onDataSetChange();
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Add video to the history
	 * 
	 * @param video
	 */
	public static void addVideoToHistory(MVideoSavedItem video, Context ctx) {
		loadVideoHistoryList(ctx);
		
		// Search video in history
		for (MVideoSavedItem v : arrHistoryVideos) {
			if (v.getUniqueId().equals(video.getUniqueId())) {
				arrHistoryVideos.remove(v);
				break;
			}
		}
		
		if (arrHistoryVideos.size() > 0) {
			arrHistoryVideos.add(0, video);
		} else {
			arrHistoryVideos.add(video);
		}
			
		saveVideoHistoryList(ctx);
	}

	/**
	 * Add video to the favorite
	 * 
	 * @param video
	 */
	public static void addVideoToFavorite(MVideoSavedItem video, Context ctx) {
		loadVideoFavoriteList(ctx);
		
		// Search video in history
		for (MVideoSavedItem v : arrFavoriteVideos) {
			if (v.getUniqueId().equals(video.getUniqueId())) {
				arrFavoriteVideos.remove(v);
				break;
			}
		}
		
		if (arrFavoriteVideos.size() > 0) {
			arrFavoriteVideos.add(0, video);
		} else {
			arrFavoriteVideos.add(video);
		}
		
		saveVideoFavoriteList(ctx);
	}
	
	public static ArrayList<MCategoryReviewItem> arrCategoryReviewItems = new ArrayList<MCategoryReviewItem>();
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	public static DisplayImageOptions options = new DisplayImageOptions.Builder()
													.cacheInMemory(true)
													.cacheOnDisc(true)
													.considerExifParams(true)
													.build();
	
	// ON LOAD IMAGE COMPLETED ANIMATION	
	public static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		private List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
		
		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
