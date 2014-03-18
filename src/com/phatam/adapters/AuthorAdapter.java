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

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.phatam.R;
import com.phatam.entities.ArtistItem;

public class AuthorAdapter extends BaseAdapter {

	private final Activity context;
	private ArrayList<ArtistItem> mArrayListArtistItems;
	private int mEndIndex = -1;

	public AuthorAdapter(Activity context, ArrayList<ArtistItem> listItem) {
		this.context = context;
		this.mArrayListArtistItems = listItem;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_item_author, null);
		}

		TextView tvArtistName = (TextView) rowView.findViewById(R.id.tvArtistName);
		tvArtistName.setText(mArrayListArtistItems.get(position).getAristName());

		TextView tvVideoCount = (TextView) rowView.findViewById(R.id.tvVideoCount);
		tvVideoCount.setText(mArrayListArtistItems.get(position).getVideoCount() + " video");

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

}