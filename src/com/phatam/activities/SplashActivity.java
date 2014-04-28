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
package com.phatam.activities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.phatam.R;
import com.phatam.config.GlobalData;
import com.phatam.model.MCategoryReviewItem;
import com.phatam.model.MVideoItem;
import com.phatam.util.UtilApplication;
import com.phatam.websevice.ApiUrl;

public class SplashActivity extends Activity implements AnimationListener {
	public final static String CLASS_PATH = SplashActivity.class.toString();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		/**
		 * Initial image loader
		 */
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		// Initialize ImageLoader with configuration.
		GlobalData.imageLoader = ImageLoader.getInstance();
		GlobalData.imageLoader.init(config);
		
		
		// Phap-Thoai Category
		MCategoryReviewItem categoryReviewModel1 = new MCategoryReviewItem();
		categoryReviewModel1.setCategoryType(MCategoryReviewItem.TYPE_SIX_VIDEOS);
		categoryReviewModel1.setCategoryId(getResources().getStringArray(R.array.arr_cartegory_id)[8]);
		categoryReviewModel1.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getResources().getStringArray(R.array.arr_cartegory_id)[8], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		categoryReviewModel1.setCategoryName(getResources().getStringArray(R.array.arr_cartegory_name)[8]);			
		GlobalData.arrCategoryReviewItems.add(categoryReviewModel1);
		
		// Kinh-Chu Category
		MCategoryReviewItem categoryReviewModel2 = new MCategoryReviewItem();
		categoryReviewModel2.setCategoryType(MCategoryReviewItem.TYPE_FOUR_VIDEOS);
		categoryReviewModel2.setCategoryId(getResources().getStringArray(R.array.arr_cartegory_id)[7]);
		categoryReviewModel2.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getResources().getStringArray(R.array.arr_cartegory_id)[7], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		categoryReviewModel2.setCategoryName(getResources().getStringArray(R.array.arr_cartegory_name)[7]);
		GlobalData.arrCategoryReviewItems.add(categoryReviewModel2);
		
		// Sach-Noi Category
		MCategoryReviewItem categoryReviewModel3 = new MCategoryReviewItem();
		categoryReviewModel3.setCategoryType(MCategoryReviewItem.TYPE_FOUR_VIDEOS);
		categoryReviewModel3.setCategoryId(getResources().getStringArray(R.array.arr_cartegory_id)[9]);
		categoryReviewModel3.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getResources().getStringArray(R.array.arr_cartegory_id)[9], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		categoryReviewModel3.setCategoryName(getResources().getStringArray(R.array.arr_cartegory_name)[9]);			
		GlobalData.arrCategoryReviewItems.add(categoryReviewModel3);
		
		// Tan-Nhac-Phat-Giao Category
		MCategoryReviewItem categoryReviewModel4 = new MCategoryReviewItem();
		categoryReviewModel4.setCategoryType(MCategoryReviewItem.TYPE_FOUR_VIDEOS);
		categoryReviewModel4.setCategoryId(getResources().getStringArray(R.array.arr_cartegory_id)[3]);
		categoryReviewModel4.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getResources().getStringArray(R.array.arr_cartegory_id)[3], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		categoryReviewModel4.setCategoryName(getResources().getStringArray(R.array.arr_cartegory_name)[3]);
		GlobalData.arrCategoryReviewItems.add(categoryReviewModel4);
		
		// Co-Nhac-Phat-Giao Category
		MCategoryReviewItem categoryReviewModel5 = new MCategoryReviewItem();
		categoryReviewModel5.setCategoryType(MCategoryReviewItem.TYPE_FOUR_VIDEOS);
		categoryReviewModel5.setCategoryId(getResources().getStringArray(R.array.arr_cartegory_id)[4]);
		categoryReviewModel5.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getResources().getStringArray(R.array.arr_cartegory_id)[4], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		categoryReviewModel5.setCategoryName(getResources().getStringArray(R.array.arr_cartegory_name)[4]);
		GlobalData.arrCategoryReviewItems.add(categoryReviewModel5);
		
		// Nhac-Thien Category
		MCategoryReviewItem categoryReviewModel6 = new MCategoryReviewItem();
		categoryReviewModel6.setCategoryType(MCategoryReviewItem.TYPE_FOUR_VIDEOS);
		categoryReviewModel6.setCategoryId(getResources().getStringArray(R.array.arr_cartegory_id)[5]);
		categoryReviewModel6.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getResources().getStringArray(R.array.arr_cartegory_id)[5], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		categoryReviewModel6.setCategoryName(getResources().getStringArray(R.array.arr_cartegory_name)[5]);
		GlobalData.arrCategoryReviewItems.add(categoryReviewModel6);
		
		// Phim-Truyen Category
		MCategoryReviewItem categoryReviewModel7 = new MCategoryReviewItem();
		categoryReviewModel7.setCategoryType(MCategoryReviewItem.TYPE_FOUR_VIDEOS);
		categoryReviewModel7.setCategoryId(getResources().getStringArray(R.array.arr_cartegory_id)[1]);
		categoryReviewModel7.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getResources().getStringArray(R.array.arr_cartegory_id)[1], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		categoryReviewModel7.setCategoryName(getResources().getStringArray(R.array.arr_cartegory_name)[1]);
		GlobalData.arrCategoryReviewItems.add(categoryReviewModel7);
		
		// Phim-Tai-Lieu Category
		MCategoryReviewItem categoryReviewModel8 = new MCategoryReviewItem();
		categoryReviewModel8.setCategoryType(MCategoryReviewItem.TYPE_FOUR_VIDEOS);
		categoryReviewModel8.setCategoryId(getResources().getStringArray(R.array.arr_cartegory_id)[2]);
		categoryReviewModel8.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getResources().getStringArray(R.array.arr_cartegory_id)[2], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		categoryReviewModel8.setCategoryName(getResources().getStringArray(R.array.arr_cartegory_name)[2]);
		GlobalData.arrCategoryReviewItems.add(categoryReviewModel8);
		
		// Phat-Phap-Nhiem-Mau Category
		MCategoryReviewItem categoryReviewModel9 = new MCategoryReviewItem();
		categoryReviewModel9.setCategoryType(MCategoryReviewItem.TYPE_FOUR_VIDEOS);
		categoryReviewModel9.setCategoryId(getResources().getStringArray(R.array.arr_cartegory_id)[0]);
		categoryReviewModel9.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getResources().getStringArray(R.array.arr_cartegory_id)[0], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		categoryReviewModel9.setCategoryName(getResources().getStringArray(R.array.arr_cartegory_name)[0]);
		GlobalData.arrCategoryReviewItems.add(categoryReviewModel9);
		
		// Anh-Sang-Phat-Phap Category
		MCategoryReviewItem categoryReviewModel10 = new MCategoryReviewItem();
		categoryReviewModel10.setCategoryType(MCategoryReviewItem.TYPE_FOUR_VIDEOS);
		categoryReviewModel10.setCategoryId(getResources().getStringArray(R.array.arr_cartegory_id)[6]);
		categoryReviewModel10.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getResources().getStringArray(R.array.arr_cartegory_id)[6], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		categoryReviewModel10.setCategoryName(getResources().getStringArray(R.array.arr_cartegory_name)[6]);
		GlobalData.arrCategoryReviewItems.add(categoryReviewModel10);
		
		// Mot-Ngay-An-Lac Category
		MCategoryReviewItem categoryReviewModel11 = new MCategoryReviewItem();
		categoryReviewModel11.setCategoryType(MCategoryReviewItem.TYPE_FOUR_VIDEOS);
		categoryReviewModel11.setCategoryId(getResources().getStringArray(R.array.arr_cartegory_id)[10]);
		categoryReviewModel11.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getResources().getStringArray(R.array.arr_cartegory_id)[10], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		categoryReviewModel11.setCategoryName(getResources().getStringArray(R.array.arr_cartegory_name)[10]);
		GlobalData.arrCategoryReviewItems.add(categoryReviewModel11);
		
		// Phim-Hoat-Hinh Category
		MCategoryReviewItem categoryReviewModel12 = new MCategoryReviewItem();
		categoryReviewModel12.setCategoryType(MCategoryReviewItem.TYPE_FOUR_VIDEOS);
		categoryReviewModel12.setCategoryId(getResources().getStringArray(R.array.arr_cartegory_id)[11]);
		categoryReviewModel12.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getResources().getStringArray(R.array.arr_cartegory_id)[11], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		categoryReviewModel12.setCategoryName(getResources().getStringArray(R.array.arr_cartegory_name)[11]);
		GlobalData.arrCategoryReviewItems.add(categoryReviewModel12);
		
		Animation zoomAnimation = AnimationUtils.loadAnimation(this, R.animator.anim_splash);
		zoomAnimation.setAnimationListener(this);
		((ImageView)findViewById(R.id.imgLogo)).startAnimation(zoomAnimation);
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// Open MainActivity when zoom animation end
		Intent mainActivityIntent = new Intent(this, MainActivity.class);
		this.startActivity(mainActivityIntent);
		this.overridePendingTransition(0, 0);
		this.finish();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Load global data
		GlobalData.loadVideoHistoryList(this);
		GlobalData.loadVideoFavoriteList(this);
		new GetAllHomePreviewVideoInfo().execute();
		
	}
	
	private void getVideoInfo(MCategoryReviewItem param) {
		ArrayList<MVideoItem> mVideoItems = new ArrayList<MVideoItem>();
		if (param.getUrlGetVideoList() == null) {
			return;
		}

		// Get video JSON info
		String response = "";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(param.getUrlGetVideoList());
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
			UtilApplication.saveJsonToInternalMemory(result.toString(), CLASS_PATH + "_" + param.getCategoryId() + ".json", this);
			

		} catch (Exception e) {
			// Announce Connection error
			
			// Load saved data
			result = UtilApplication.loadJsonFromInternalMemory(CLASS_PATH + "_" + param.getCategoryId() + ".json", this);
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
				int site_views = line_object.getInt("site_views");
				String yt_id = line_object.getString("yt_id");
				String yt_thumb = "http://img.youtube.com/vi/" + yt_id + "/mqdefault.jpg";
				String category = line_object.getString("category");
				MVideoItem item = new MVideoItem(uniq_id, artist, video_title,
						description, yt_id, yt_thumb, site_views, category);

				mVideoItems.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		param.setLoadedVideoList(mVideoItems);
	}


	/**
	 * This is Asynctask get all the home preview video info run in Background
	 * 
	 * @author anhle
	 * 
	 */
	protected class GetAllHomePreviewVideoInfo extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... params) {
			for (MCategoryReviewItem categoryReview : GlobalData.arrCategoryReviewItems) {
				getVideoInfo(categoryReview);
			}
			
			return null;			
		}

		@Override
		protected void onPostExecute(Void r) {

		}
	}
}