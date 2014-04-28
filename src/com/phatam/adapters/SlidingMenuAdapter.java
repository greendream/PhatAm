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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.phatam.R;
import com.phatam.model.MSlidingMenuListItem;

public class SlidingMenuAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<MSlidingMenuListItem> mSlidingMenuListItems;

	/**
	 * Constructor
	 * 
	 * @param c
	 */
	public SlidingMenuAdapter(Context c, ArrayList<MSlidingMenuListItem> navigationDrawerListItems) {
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
		case MSlidingMenuListItem.ITEM_TYPE_TOP_MENU_GROUP:
			
			listItem = inflater.inflate(R.layout.sliding_menu_home_item, parent, false);
			
			ImageButton ibHome = (ImageButton) listItem.findViewById(R.id.ibHome);
			ibHome.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mSlidingMenuListItems.get(0).getActionHome().doAction();
				}
			});
			
			ImageButton ibFavorite = (ImageButton) listItem.findViewById(R.id.ibFavorite);
			ibFavorite.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mSlidingMenuListItems.get(0).getActionFavorite().doAction();
				}
			});

			ImageButton ibHistory = (ImageButton) listItem.findViewById(R.id.ibHistory);
			ibHistory.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mSlidingMenuListItems.get(0).getActionHistory().doAction();
				}
			});

			listItem.setOnClickListener(null);
			break;

		
		case MSlidingMenuListItem.ITEM_TYPE_SCREEN_IN_CATEGORY:
			
			listItem = inflater.inflate(R.layout.sliding_menu_screen_item, parent, false);
			
			ImageView ivIconLeft = (ImageView) listItem.findViewById(R.id.ivIconLeft);
			ivIconLeft.setImageResource(mSlidingMenuListItems.get(position).getIconLeft());
			
			TextView tvText = (TextView) listItem.findViewById(R.id.tvText);
			tvText.setText(mSlidingMenuListItems.get(position).getText());

			TextView tvCounter = (TextView) listItem.findViewById(R.id.tvCounter);
			tvCounter.setText(mSlidingMenuListItems.get(position).getCounter());
			
			break;
			
			
		case MSlidingMenuListItem.ITEM_TYPE_CATEGORY_NAME:
			
			listItem = inflater.inflate(R.layout.sliding_menu_category_name_item, parent, false);
			listItem.setOnClickListener(null);
			TextView tvCategoryName = (TextView) listItem.findViewById(R.id.tvCategoryName);
			tvCategoryName.setText(mSlidingMenuListItems.get(position).getText().toUpperCase());
			break;
		}
		
		return listItem;
	}
}
