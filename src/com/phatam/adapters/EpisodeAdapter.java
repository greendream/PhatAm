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
package com.phatam.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.phatam.R;
import com.phatam.entities.Episode;
import com.phatam.entities.VideoItem;

public class EpisodeAdapter extends ArrayAdapter<Episode> {

	private final Activity mActivity;
	private VideoItem mVideoItem;
	private ArrayList<Episode> mEpisodeData;
	DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private int mEndIndex = -1;
	
	public EpisodeAdapter(Activity activity, VideoItem videoItem) {
		// TODO Auto-generated constructor stub
		super(activity, R.layout.list_item_episode, videoItem.getEpisodes());
		mVideoItem = videoItem;
		this.mActivity = activity;
		this.mEpisodeData = videoItem.getEpisodes();
		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(10)).build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_item_episode, null);
		}
			
		((TextView) rowView.findViewById(R.id.episode_index)).setText("Phần " + mEpisodeData.get(position).getEpisodeId());
		((TextView) rowView.findViewById(R.id.video_list_astist)).setText("Tác giả: " + mVideoItem.getVideoArtist());
		imageLoader.displayImage(mEpisodeData.get(position).getYoutubeThumb(),
				((ImageView) rowView.findViewById(R.id.video_list_image)), options, animateFirstListener);
		
		if (position > mEndIndex) {
			rowView.startAnimation(AnimationUtils.loadAnimation(mActivity, R.animator.push_up_in));
			mEndIndex = position;
		}
		
		return rowView;
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

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