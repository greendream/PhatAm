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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phatam.R;
import com.phatam.entities.SlidingMenuListItem;

public class SlidingMenuAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<SlidingMenuListItem> mSlidingMenuListItems;

	/**
	 * Constructor
	 * 
	 * @param c
	 */
	public SlidingMenuAdapter(Context c, ArrayList<SlidingMenuListItem> navigationDrawerListItems) {
		mContext = c;
		mSlidingMenuListItems = navigationDrawerListItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mSlidingMenuListItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// inflater to create the List Item
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View listItem = null;
		
		switch (mSlidingMenuListItems.get(position).getType()) {
		case SlidingMenuListItem.HOME_ITEM:
			
			listItem = inflater.inflate(R.layout.sliding_menu_home_item, parent, false);
			
			ImageView ivIconHome = (ImageView) listItem.findViewById(R.id.ivIconHome);
			ivIconHome.setImageResource(mSlidingMenuListItems.get(position).getIconLeft());
			
			TextView tvHomeText = (TextView) listItem.findViewById(R.id.tvHomeText);
			tvHomeText.setText(mSlidingMenuListItems.get(position).getText());

			TextView tvHomeParam = (TextView) listItem.findViewById(R.id.tvHomeParam);
			tvHomeParam.setText(mSlidingMenuListItems.get(position).getCounter());
			
			break;

		
		case SlidingMenuListItem.SCREEN_IN_CATEGORY:
			
			listItem = inflater.inflate(R.layout.sliding_menu_screen_item, parent, false);
			
			ImageView ivIconLeft = (ImageView) listItem.findViewById(R.id.ivIconLeft);
			ivIconLeft.setImageResource(mSlidingMenuListItems.get(position).getIconLeft());
			
			TextView tvText = (TextView) listItem.findViewById(R.id.tvText);
			tvText.setText(mSlidingMenuListItems.get(position).getText());

			TextView tvCounter = (TextView) listItem.findViewById(R.id.tvCounter);
			tvCounter.setText(mSlidingMenuListItems.get(position).getCounter());
			
			break;
			
			
		case SlidingMenuListItem.CATEGORY_NAME:
			
			listItem = inflater.inflate(R.layout.sliding_menu_category_name_item, parent, false);
			listItem.setOnClickListener(null);
			TextView tvCategoryName = (TextView) listItem.findViewById(R.id.tvCategoryName);
			tvCategoryName.setText(mSlidingMenuListItems.get(position).getText().toUpperCase());
			break;
		}
		
		return listItem;
	}
}
