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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.phatam.R;
import com.phatam.customviews.RoundedImageView;
import com.phatam.model.MArtist;

public class ArtistAdapter extends BaseAdapter {

	private final Activity context;
	private ArrayList<MArtist> mArrayListArtistItems;
	private int mEndIndex = -1;

	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public ArtistAdapter(Activity context, ArrayList<MArtist> listItem) {
		this.context = context;
		this.mArrayListArtistItems = listItem;
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.icon_artist)
		.showImageForEmptyUri(R.drawable.icon_artist)
		.showImageOnFail(R.drawable.icon_artist)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_item_artist, null);
		}

		TextView tvArtistName = (TextView) rowView.findViewById(R.id.tvArtistName);
		tvArtistName.setText(mArrayListArtistItems.get(position).getArtistName());

		TextView tvVideoCount = (TextView) rowView.findViewById(R.id.tvVideoCount);
		tvVideoCount.setText(mArrayListArtistItems.get(position).getVideoCount() + " video");

		imageLoader.displayImage(mArrayListArtistItems.get(position).getAvatar(),
				((RoundedImageView) rowView.findViewById(R.id.ivArtistImage)), options, animateFirstListener);
		
		if (position > mEndIndex) {
			rowView.startAnimation(AnimationUtils.loadAnimation(context, R.animator.fade_up_in));
			mEndIndex = position;
		}

		return rowView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayListArtistItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
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